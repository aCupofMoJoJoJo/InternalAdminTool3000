package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

/**
 * Test class for Bill.
 */
class BillTest {

	private Bill bill;
	
	@BeforeEach
	void setUp()
	{
		bill = new Gson().fromJson("{\"id\":0,\"amount\":51.25,\"isInPaymentPlan\":true,\"remainingAmount\":51.25,\"nextPaymentDueDate\":\"2020-09-29\"}", Bill.class);
	}
	
	@Test
	void testGetId() {
		final int ID = 0;
		assertEquals(ID, bill.getId());
	}
	
	@Test
	void testGetAmount() {
		BigDecimal expectedAmount = new BigDecimal(51.25);
		assertEquals(expectedAmount.setScale(2, RoundingMode.CEILING), bill.getAmount());	
	}
	
	@Test
	void testGetIsInPaymentPlan() {
		assertEquals(true, bill.isInPaymentPlan());
	}
	
	@Test
	void testGetRemainingAmount() {
		BigDecimal expectedRemainingAmount = new BigDecimal(51.25);
		assertEquals(expectedRemainingAmount.setScale(2, RoundingMode.CEILING), bill.getRemainingAmount());	
	}
	
	@Test
	void testGetNextPaymentDueDate() {
		assertEquals("2020-09-29", bill.getNextPaymentDueDate());
	}
}