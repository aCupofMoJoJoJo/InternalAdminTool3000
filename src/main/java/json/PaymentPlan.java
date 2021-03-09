package json;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

import enums.InstallmentFrequency;

/**
 * Represents a Payment Plan JSON object.
 */
public class PaymentPlan {

	@SerializedName("amount_to_pay")
	private BigDecimal amountToPay;
	
	@SerializedName("debt_id")
	private int debtId;
	
	private int id;
	
	@SerializedName("installment_amount")
	private BigDecimal installmentAmount;
	
	@SerializedName("installment_frequency")
	private InstallmentFrequency installmentFrequency;
	
	@SerializedName("start_date")
	private String startDate;
	
	/**
	 * Returns the amount to pay on the payment plan.
	 * @return the amount to pay on the payment plan.
	 */
	public BigDecimal getAmountToPay() {
		return amountToPay;
	}

	/**
	 * Returns the debt id associated with the payment plan.
	 * @return the debt id associated with the payment plan.
	 */
	public int getDebtId() {
		return debtId;
	}
	
	/**
	 * Returns the id of the payment plan.
	 * @return the id of with the payment plan.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the installment amount to pay for the payment plan.
	 * @return the installment amount to pay for the payment plan.
	 */
	public BigDecimal getInstallmentAmount() {
		return installmentAmount;
	}
	
	/**
	 * Returns the installment frequency for the payment plan.
	 * @return the installment frequency for the payment plan.
	 */
	public InstallmentFrequency getInstallmentFrequency() {
		return installmentFrequency;
	}

	/**
	 * Returns the start date of the payment plan.
	 * @return the start date of the payment plan.
	 */
	public String getStartDate() {
		return startDate;
	}
}