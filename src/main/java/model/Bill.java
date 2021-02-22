package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a bill that will be sent to a debtor.
 */
public class Bill {
	private int id;
	private BigDecimal amount;
	private boolean isInPaymentPlan = false;
	private BigDecimal remainingAmount;
	private String nextPaymentDueDate = null;
	
	/**
	 * Constructor to create an instance of this class.
	 * 
	 * @param id the id of the debt
	 * @param amount the amount of the debt
	 * @param isInPaymentPlan a boolean indicator of whether or not the debt has a payment plan. True if has payment plan, false otherwise.
	 * @param remainingAmount the remaining amount of the debt
	 * @param nextPaymentDueDate the next due date for the next payment
	 */
	public Bill(int id, BigDecimal amount, boolean isInPaymentPlan, BigDecimal remainingAmount, String nextPaymentDueDate)
	{
		this.id = id;
		this.amount = amount.setScale(2, RoundingMode.DOWN);
		this.isInPaymentPlan = isInPaymentPlan;
		this.remainingAmount = remainingAmount.setScale(2, RoundingMode.DOWN);
		this.nextPaymentDueDate = nextPaymentDueDate;
	}
	
	/**
	 * Returns the id of the debt.
	 * @return the id of the debt.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the amount of the debt.
	 * @return the amount of the debt.
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Returns a boolean indicator of whether or not the debt has a payment plan.
	 * @return a boolean indicator of whether or not the debt has a payment plan.
	 */
	public boolean isInPaymentPlan() {
		return isInPaymentPlan;
	}

	/**
	 * Returns the remaining amount of the debt.
	 * @return the remaining amount of the debt.
	 */
	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}

	/**
	 * Returns the next due date for the next payment.
	 * @return the next due date for the next payment.
	 */
	public String getNextPaymentDueDate() {
		return nextPaymentDueDate;
	}
}