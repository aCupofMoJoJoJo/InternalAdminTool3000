package enums;

/**
 * Installment frequency for the payment plan.
 */
public enum InstallmentFrequency {
	
	/**
	 * Payments made each week.
	 */
	WEEKLY(1),
	
	/**
	 * Payments made every other week.
	 */
	BI_WEEKLY(2);
	
    private final int frequency;

    /**
     * Constructor for this enum.
     * @param frequency the frequency between each payment.
     */
    InstallmentFrequency(final int frequency) {
    	this.frequency = frequency;
    }

    /**
     * Returns the installment frequency for the payment plan.
     * @return the installment frequency for the payment plan.
     */
    public int getFrequency() 
    { 
    	return this.frequency; 
    }
}