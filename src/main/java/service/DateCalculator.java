package service;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Weeks;

import enums.InstallmentFrequency;

/**
 * Calculator to determine the next payment due date for a payment.
 */
public class DateCalculator {
	
	private InstallmentFrequency installmentFrequency;
	private DateTime paymentStartDateTime;
	private List<String> paymentDates;
	
	/**
	 * Constructor to create an instance of this class.
	 * 
	 * @param installmentFrequency the installment frequency for the payment plan.
	 * @param paymentStartDateTime the start date of the payment plan.
	 * @param paymentDates a list of dates in which payments have been made.
	 */
	public DateCalculator(InstallmentFrequency installmentFrequency, String paymentStartDateTime, List<String> paymentDates)
	{
		this.installmentFrequency = installmentFrequency;
		this.paymentStartDateTime = new DateTime(paymentStartDateTime, DateTimeZone.UTC );
		this.paymentDates = paymentDates;
	}
	
	/**
	 * Determines the frequency of installment plans and retrieves next payment date.
	 * @return the next payment date. Will not be null.
	 */
	public String calculate() {
		if(installmentFrequency == InstallmentFrequency.WEEKLY)
		{						
			return getNextPayment(InstallmentFrequency.WEEKLY.getFrequency()); 
		}
		else
		{			
			return getNextPayment(InstallmentFrequency.BI_WEEKLY.getFrequency()); 
		}
	}
	
	/**
	 * 
	 * @param paymentFrequency the frequency for a payment to be made. 
	 * @return the next payment date. Will not be null. 
	 */
	private String getNextPayment(int paymentFrequency)
	{
		DateTime nextPaymentDueDate = paymentStartDateTime;
		if(paymentDates == null)
		{
			return nextPaymentDueDate.toString();
		}
		
		for(int paymentNo = 0; paymentNo < paymentDates.size(); paymentNo++)
		{
			DateTime paymentDateTime = new DateTime(paymentDates.get(paymentNo), DateTimeZone.UTC );
			
			int weeks = Weeks.weeksBetween(nextPaymentDueDate.dayOfWeek().withMinimumValue().minusDays(paymentFrequency), 
					paymentDateTime.dayOfWeek().withMaximumValue().plusDays(paymentFrequency)).getWeeks();
			
			if(weeks > paymentFrequency)
			{
				nextPaymentDueDate = DateTime.now();
			}
			else {
				nextPaymentDueDate = getNextPaymentFromStartDate(paymentNo + 1, paymentFrequency);
			}
		}
		
		return nextPaymentDueDate.toString();
	}
	
	/**
	 * Adds a payment multiplier to the payment start date time and returns the new due date.
	 * @param paymentNo number of payment made in the current plan.
	 * @param paymentFrequency the frequency for a payment to be made. 
	 * @return the next payment date. Will not be null.
	 */
	private DateTime getNextPaymentFromStartDate(int paymentNo,int paymentFrequency) {
		
		int paymentMultiplier = paymentNo * paymentFrequency;
		
		return paymentStartDateTime.plusWeeks(paymentMultiplier);
	}
}