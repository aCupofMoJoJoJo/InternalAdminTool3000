package json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

/**
 * Test class for Payment.
 */
class PaymentTest {

	private Payment payment;
	
	@BeforeEach
	void setUp()
	{
		payment = new Gson().fromJson("{\"amount\":51.25,\"date\":\"2020-09-29\",\"payment_plan_id\":0}", Payment.class);
	}
	
	@Test
	void testGetAmount() {
		BigDecimal expectedAmount = new BigDecimal(51.25);
		assertEquals(expectedAmount.setScale(2, RoundingMode.DOWN), payment.getAmount());
	}
	
	@Test
	void testGetDate() {
		assertEquals("2020-09-29", payment.getDate());
	}
	
	@Test
	void testGetPaymentPlanId() {
		final int ID = 0;
		assertEquals(ID, payment.getPaymentPlanId());
	}
}