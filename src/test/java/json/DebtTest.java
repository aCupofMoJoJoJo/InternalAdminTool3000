package json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

/**
 * Test class for Debt.
 */
class DebtTest {

	private Debt debt;
	
	@BeforeEach
	void setUp()
	{
		debt = new Gson().fromJson("{\"amount\":123.42,\"id\":0}", Debt.class);
	}
	
	@Test
	void testGetAmount() {
		BigDecimal expectedAmount = new BigDecimal(123.42);
		assertEquals(expectedAmount.setScale(2, RoundingMode.DOWN), debt.getAmount());		
	}
	
	@Test
	void testGetId() {
		final int ID = 0;
		assertEquals(ID, debt.getId());		
	}
}