package json;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a Payment JSON object.
 */
public class Payment {

	private BigDecimal amount;
	
	private String date;

	@SerializedName("payment_plan_id")
	private int paymentPlanId;
	
	/**
	 * Returns the amount of the payment.
	 * @return the amount of the payment.
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	
	/**
	 * Returns the date of the payment.
	 * @return the date of the payment.
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * Returns the id of the payment plan.
	 * @return the id of the payment plan.
	 */
	public int getPaymentPlanId() {
		return paymentPlanId;
	}
}