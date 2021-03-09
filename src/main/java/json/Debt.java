package json;

import java.math.BigDecimal;

/**
 * Represents a Debt JSON object.
 */
public class Debt {

	private BigDecimal amount;
	
	private int id;

	/**
	 * Returns the amount of the debt.
	 * @return the amount of the debt.
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	
	/**
	 * Returns the id of the debt.
	 * @return the id of the debt.
	 */
	public int getId() {
		return id;
	}
}