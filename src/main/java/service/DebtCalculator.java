package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import enums.InstallmentFrequency;
import json.Debt;
import json.Payment;
import json.PaymentPlan;
import model.Bill;

/**
 * Calculator to determine the debt of the debtor and build out a bill.
 */
public class DebtCalculator {
	private int id;
	private BigDecimal amount;
	private BigDecimal amountToPay;
	private InstallmentFrequency installmentFrequency;
	private String startDate;
	private List<Payment> payments;
	
	/**
	 * Constructor to create an instance of this class.
	 * 
	 * @param debt the current debt of the debtor
	 * @param paymentPlan the payment plan associated to the debt
	 * @param payments the list of payments made to the payment plan
	 */
	public DebtCalculator(Debt debt, PaymentPlan paymentPlan, List<Payment> payments) {
		this.id = debt.getId();
		this.amount = debt.getAmount();
		this.amountToPay = paymentPlan.getAmountToPay();
		this.installmentFrequency = paymentPlan.getInstallmentFrequency();
		this.startDate = paymentPlan.getStartDate();
		this.payments = payments;
	}
	
	/**
	 * Calculates and generates a bill for the debtor.
	 * @return the bill for a debtor.
	 */
	public Bill calculate()
	{
		String nextPaymentDueDate = DateTime.now().toString();
		boolean isInPaymentPlan = false;
		BigDecimal remainingAmount = this.amount;
		
		if(payments != null && payments.size() > 0)
		{
			isInPaymentPlan = true;
			remainingAmount = this.amountToPay;
			
			List<String> paymentDateTime = new ArrayList<String>();
			
			
			for(Payment payment : payments)
			{				
				remainingAmount = remainingAmount.subtract(payment.getAmount());
				
				if(remainingAmount.compareTo(BigDecimal.ZERO) == 0)
				{
					nextPaymentDueDate = null;
					break;
				}
				
				paymentDateTime.add(payment.getDate());
			}
			
			if(nextPaymentDueDate != null)
			{
				DateCalculator dateCalculator = new DateCalculator(installmentFrequency, this.startDate, paymentDateTime);
				nextPaymentDueDate = dateCalculator.calculate();				
			}
		}
		
		return new Bill(this.id, this.amount, isInPaymentPlan, remainingAmount, nextPaymentDueDate);
	}
}