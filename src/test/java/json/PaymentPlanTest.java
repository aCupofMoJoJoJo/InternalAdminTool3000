package json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import enums.InstallmentFrequency;

/**
 * Test class for PaymentPlan.
 */
class PaymentPlanTest {
	
	private PaymentPlan paymentPlan;

	@BeforeEach
	void setUp()
	{
		paymentPlan = new Gson().fromJson("{\"amount_to_pay\":102.50,\"debt_id\":0,\"id\":0,\"installment_amount\":51.25,\"installment_frequency\":\"WEEKLY\",\"start_date\":\"2020-09-28\"}", PaymentPlan.class);
	}
	
	@Test
	void testGetAmountToPay() {
		BigDecimal expectedAmountToPay = new BigDecimal(102.50);
		assertEquals(expectedAmountToPay.setScale(2, RoundingMode.DOWN), paymentPlan.getAmountToPay());	
	}
	
	@Test
	void testGetDebtId() {
		final int ID = 0;
		assertEquals(ID, paymentPlan.getDebtId());
	}
	
	@Test
	void testGetId() {
		final int ID = 0;
		assertEquals(ID, paymentPlan.getId());
	}
	
	@Test
	void testGetInstallmentAmount() {
		BigDecimal expectedInstallmentAmount = new BigDecimal(51.25);
		assertEquals(expectedInstallmentAmount.setScale(2, RoundingMode.DOWN), paymentPlan.getInstallmentAmount());
	}
	
	@Test
	void testGetInstallmentFrequency() {
		assertEquals(InstallmentFrequency.WEEKLY, paymentPlan.getInstallmentFrequency());
	}
	
	@Test
	void testGetStartDate() {
		assertEquals("2020-09-28", paymentPlan.getStartDate());
	}
}