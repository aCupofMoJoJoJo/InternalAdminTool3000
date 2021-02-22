package service;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import json.Debt;
import json.Payment;
import json.PaymentPlan;
import model.Bill;

/**
 * Test class for DebtCalculator.
 */
class DebtCalculatorTest {

	@BeforeEach
	void setUp()
	{
		DateTimeUtils.setCurrentMillisFixed(10L);
	}
	
	@AfterEach
	void tearDown() {
		DateTimeUtils.setCurrentMillisSystem();
	}
	
	@Test
	void testConstructor() {		
		DebtCalculator debtCalculator = new DebtCalculator(mock(Debt.class), mock(PaymentPlan.class), Arrays.asList(mock(Payment.class)));
		assertTrue(debtCalculator instanceof DebtCalculator);
	}
	
	@Test
	void testCalculate_WhenThereHaveBeenNoPaymentsMadeToAPaymentPlan() {
		BigDecimal amount = new BigDecimal(102.50);
		BigDecimal remainingAmount = new BigDecimal(102.50);
		
		Bill expectedBill = new Bill(0, amount.setScale(2, RoundingMode.DOWN), false, remainingAmount, DateTime.now().toString());
		
		Debt dummyDebt = new Gson().fromJson("{\"amount\":102.50,\"id\":0}", Debt.class);
		PaymentPlan dummyPaymentPlan = new Gson().fromJson("{\"amount_to_pay\":102.50,\"debt_id\":0,\"id\":0,\"installment_amount\":51.25,\"installment_frequency\":\"WEEKLY\",\"start_date\":\"2020-09-29\"}", PaymentPlan.class);
		
		DebtCalculator debtCalculator = new DebtCalculator(dummyDebt, dummyPaymentPlan, Collections.emptyList());
		Bill actualBill = debtCalculator.calculate();
		
		assertEquals(expectedBill.getAmount(), actualBill.getAmount());
		assertEquals(expectedBill.getId(), actualBill.getId());
		assertEquals(expectedBill.isInPaymentPlan(), actualBill.isInPaymentPlan());
		assertEquals(expectedBill.getRemainingAmount(), actualBill.getRemainingAmount());
		assertEquals(expectedBill.getNextPaymentDueDate(), actualBill.getNextPaymentDueDate());
	}
	
	@Test
	void testCalculate_WhenNullIsPassedForPayments() {
		BigDecimal amount = new BigDecimal(102.50);
		BigDecimal remainingAmount = new BigDecimal(102.50);
		
		Bill expectedBill = new Bill(0, amount.setScale(2, RoundingMode.DOWN), false, remainingAmount, DateTime.now().toString());
		
		Debt dummyDebt = new Gson().fromJson("{\"amount\":102.50,\"id\":0}", Debt.class);
		PaymentPlan dummyPaymentPlan = new Gson().fromJson("{\"amount_to_pay\":102.50,\"debt_id\":0,\"id\":0,\"installment_amount\":51.25,\"installment_frequency\":\"WEEKLY\",\"start_date\":\"2020-09-29\"}", PaymentPlan.class);
		
		DebtCalculator debtCalculator = new DebtCalculator(dummyDebt, dummyPaymentPlan, null);
		Bill actualBill = debtCalculator.calculate();
		
		assertEquals(expectedBill.getAmount(), actualBill.getAmount());
		assertEquals(expectedBill.getId(), actualBill.getId());
		assertEquals(expectedBill.isInPaymentPlan(), actualBill.isInPaymentPlan());
		assertEquals(expectedBill.getRemainingAmount(), actualBill.getRemainingAmount());
		assertEquals(expectedBill.getNextPaymentDueDate(), actualBill.getNextPaymentDueDate());
	}
	
	@Test
	void testCalculate_WhenOnePaymentHasBeenMade() {
		BigDecimal amount = new BigDecimal(102.50);
		BigDecimal remainingAmount = new BigDecimal(51.25);
		Bill expectedBill = new Bill(0, amount, true, remainingAmount, "2020-10-06T00:00:00.000Z");
		
		Debt dummyDebt = new Gson().fromJson("{\"amount\":102.50,\"id\":0}", Debt.class);
		PaymentPlan dummyPaymentPlan = new Gson().fromJson("{\"amount_to_pay\":102.50,\"debt_id\":0,\"id\":0,\"installment_amount\":51.25,\"installment_frequency\":\"WEEKLY\",\"start_date\":\"2020-09-29\"}", PaymentPlan.class);
		Payment dummyPayment = new Gson().fromJson("{\"amount\":51.25,\"date\":\"2020-09-29\",\"payment_plan_id\":0}", Payment.class);
		
		DebtCalculator debtCalculator = new DebtCalculator(dummyDebt, dummyPaymentPlan, Arrays.asList(dummyPayment));
		Bill actualBill = debtCalculator.calculate();
		
		assertEquals(expectedBill.getAmount(), actualBill.getAmount());
		assertEquals(expectedBill.getId(), actualBill.getId());
		assertEquals(expectedBill.isInPaymentPlan(), actualBill.isInPaymentPlan());
		assertEquals(expectedBill.getRemainingAmount(), actualBill.getRemainingAmount());
		assertEquals(expectedBill.getNextPaymentDueDate(), actualBill.getNextPaymentDueDate());
	}
	
	@Test
	void testCalculate_WhenTwoPaymentsHasBeenMade() {
		BigDecimal amount = new BigDecimal(102.40);		
		BigDecimal remainingAmount = new BigDecimal(51.20);
		Bill expectedBill = new Bill(0, amount, true, remainingAmount.setScale(2, RoundingMode.DOWN), "2020-10-13T00:00:00.000Z");
		
		Debt dummyDebt = new Gson().fromJson("{\"amount\":102.40,\"id\":0}", Debt.class);
		PaymentPlan dummyPaymentPlan = new Gson().fromJson("{\"amount_to_pay\":102.40,\"debt_id\":0,\"id\":0,\"installment_amount\":25.60,\"installment_frequency\":\"WEEKLY\",\"start_date\":\"2020-09-29\"}", PaymentPlan.class);
		Payment dummyPayment1 = new Gson().fromJson("{\"amount\":25.60,\"date\":\"2020-09-29\",\"payment_plan_id\":0}", Payment.class);
		Payment dummyPayment2 = new Gson().fromJson("{\"amount\":25.60,\"date\":\"2020-10-06\",\"payment_plan_id\":0}", Payment.class);
		
		DebtCalculator debtCalculator = new DebtCalculator(dummyDebt, dummyPaymentPlan, Arrays.asList(dummyPayment1, dummyPayment2));
		Bill actualBill = debtCalculator.calculate();
		
		assertEquals(expectedBill.getAmount(), actualBill.getAmount());
		assertEquals(expectedBill.getId(), actualBill.getId());
		assertEquals(expectedBill.isInPaymentPlan(), actualBill.isInPaymentPlan());
		assertEquals(expectedBill.getRemainingAmount(), actualBill.getRemainingAmount());
		assertEquals(expectedBill.getNextPaymentDueDate(), actualBill.getNextPaymentDueDate());
	}
	
	@Test
	void testCalculate_WhenPaymentHasBeenPaidOff() {
		BigDecimal amount = new BigDecimal(51.25);
		BigDecimal remainingAmount = new BigDecimal(0);
		Bill expectedBill = new Bill(0, amount, true, remainingAmount, null);
		
		Debt dummyDebt = new Gson().fromJson("{\"amount\":51.25,\"id\":0}", Debt.class);
		PaymentPlan dummyPaymentPlan = new Gson().fromJson("{\"amount_to_pay\":51.25,\"debt_id\":0,\"id\":0,\"installment_amount\":51.25,\"installment_frequency\":\"WEEKLY\",\"start_date\":\"2020-09-29\"}", PaymentPlan.class);
		Payment dummyPayment = new Gson().fromJson("{\"amount\":51.25,\"date\":\"2020-09-29\",\"payment_plan_id\":0}", Payment.class);
		
		DebtCalculator debtCalculator = new DebtCalculator(dummyDebt, dummyPaymentPlan, Arrays.asList(dummyPayment));
		Bill actualBill = debtCalculator.calculate();
		
		assertEquals(expectedBill.getAmount(), actualBill.getAmount());
		assertEquals(expectedBill.getId(), actualBill.getId());
		assertEquals(expectedBill.isInPaymentPlan(), actualBill.isInPaymentPlan());
		assertEquals(expectedBill.getRemainingAmount(), actualBill.getRemainingAmount());
		assertEquals(expectedBill.getNextPaymentDueDate(), actualBill.getNextPaymentDueDate());
	}
}