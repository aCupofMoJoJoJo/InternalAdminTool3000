package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import enums.InstallmentFrequency;

/**
 * Test class for DateCalculator.
 */
public class DateCalculatorTest {
	
	private static final String TEST_DATE_ONE = "2020-09-28";
	private static final String TEST_DATE_TWO = "2020-10-05";
	
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
		
		DateCalculator dateCalculator = new DateCalculator(InstallmentFrequency.WEEKLY, TEST_DATE_ONE, Arrays.asList(TEST_DATE_ONE));
		
		assertTrue(dateCalculator instanceof DateCalculator);
	}
	
	@Test
	void testCalculate_ForWeeklyPaymentPlan_OnePayment()
	{
		DateCalculator dateCalculator = new DateCalculator(InstallmentFrequency.WEEKLY, TEST_DATE_ONE, Arrays.asList(TEST_DATE_ONE));
		String actualDate = dateCalculator.calculate();
		
		final String expectedDateString = "2020-10-05T00:00:00.000Z";
		
		assertEquals(expectedDateString, actualDate);
	}
	
	@Test
	void testCalculate_ForWeeklyPaymentPlan_TwoPayments()
	{
		DateCalculator dateCalculator = new DateCalculator(InstallmentFrequency.WEEKLY, TEST_DATE_ONE, Arrays.asList(TEST_DATE_ONE, TEST_DATE_TWO));
		String actualDate = dateCalculator.calculate();
		
		final String expectedDateString = "2020-10-12T00:00:00.000Z";
		
		assertEquals(expectedDateString, actualDate);
	}
	
	@Test
	void testCalculate_ForBiWeeklyPaymentPlan_OnePayment()
	{
		DateCalculator dateCalculator = new DateCalculator(InstallmentFrequency.BI_WEEKLY, TEST_DATE_ONE, Arrays.asList(TEST_DATE_ONE));
		String actualDate = dateCalculator.calculate();
		
		final String expectedDateString = "2020-10-12T00:00:00.000Z";
		
		assertEquals(expectedDateString, actualDate);
	}
	
	@Test
	void testCalculate_ForBiWeeklyPaymentPlan_TwoPayments()
	{
		DateCalculator dateCalculator = new DateCalculator(InstallmentFrequency.BI_WEEKLY, TEST_DATE_ONE, Arrays.asList(TEST_DATE_ONE, TEST_DATE_TWO));
		String actualDate = dateCalculator.calculate();
		
		final String expectedDateString = "2020-10-26T00:00:00.000Z";
		
		assertEquals(expectedDateString, actualDate);
	}
	
	@Test
	void testCalculate_ForWeeklyPaymentPlan_OnePayment_DebtorIsBehindOnPayments()
	{
		DateCalculator dateCalculator = new DateCalculator(InstallmentFrequency.WEEKLY, TEST_DATE_ONE, Arrays.asList(TEST_DATE_TWO));
		String actualDate = dateCalculator.calculate();
		
		String expectedDateString = DateTime.now().toString();
		
		assertEquals(expectedDateString, actualDate);
	}
	
	@Test
	void testCalculate_ForWeeklyPaymentPlan_TwoPayments_DebtorIsBehindOnPayments()
	{
		final String TEST_DATE_THREE = "2020-11-15";
		DateCalculator dateCalculator = new DateCalculator(InstallmentFrequency.WEEKLY, TEST_DATE_ONE, Arrays.asList(TEST_DATE_TWO, TEST_DATE_THREE));
		String actualDate = dateCalculator.calculate();
		
		String expectedDateString = DateTime.now().toString();
		
		assertEquals(expectedDateString, actualDate);
	}
	
	@Test
	void testCalculate_ForBiWeeklyPaymentPlan_OnePayment_DebtorIsBehindOnPayments()
	{
		final String TEST_DATE_FOUR = "2020-10-15";
		DateCalculator dateCalculator = new DateCalculator(InstallmentFrequency.BI_WEEKLY, TEST_DATE_ONE, Arrays.asList(TEST_DATE_FOUR));
		String actualDate = dateCalculator.calculate();
		
		String expectedDateString = DateTime.now().toString();
		
		assertEquals(expectedDateString, actualDate);
	}
	
	@Test
	void testCalculate_ForBiWeeklyPaymentPlan_TwoPayments_DebtorIsBehindOnPayments()
	{
		final String TEST_DATE_THREE = "2020-11-15";
		final String TEST_DATE_FOUR = "2020-10-15";
		DateCalculator dateCalculator = new DateCalculator(InstallmentFrequency.BI_WEEKLY, TEST_DATE_ONE, Arrays.asList(TEST_DATE_FOUR, TEST_DATE_THREE));
		String actualDate = dateCalculator.calculate();
		
		String expectedDateString = DateTime.now().toString();
		
		assertEquals(expectedDateString, actualDate);
	}
	
	@Test
	void testCalculate_NoPaymentsHaveBeenMade()
	{
		DateCalculator dateCalculator = new DateCalculator(InstallmentFrequency.WEEKLY, TEST_DATE_ONE, Collections.emptyList());
		String actualDate = dateCalculator.calculate();
		
		final String expectedDateString = "2020-09-28T00:00:00.000Z";
		
		assertEquals(expectedDateString, actualDate);
	}
	
	@Test
	void testCalculate_NullIsPassedForPayments()
	{
		DateCalculator dateCalculator = new DateCalculator(InstallmentFrequency.WEEKLY, TEST_DATE_ONE, null);
		String actualDate = dateCalculator.calculate();
		
		final String expectedDateString = "2020-09-28T00:00:00.000Z";
		
		assertEquals(expectedDateString, actualDate);
	}
}