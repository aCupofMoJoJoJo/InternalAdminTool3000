package admin;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import exception.BadRequestException;
import http.HttpClientWrapper;
import json.Debt;
import json.Payment;
import json.PaymentPlan;

/**
 * Test class for Operation.
 */
class OperationTest {
	
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	
	private HttpClientWrapper mockClientWrapper = mock(HttpClientWrapper.class);
	
	@BeforeEach
	void setUp() {
		System.setOut(new PrintStream(outputStreamCaptor));
	}
	
	@Test
	void testConstructor_IsInstanceOfOperation() {
		Operation operation = new Operation(mockClientWrapper);
		
		assertTrue(operation instanceof Operation);
	}
	
	@Test
	void testConstructor_WhenNullIsPassed() {
		new Operation(null);
		
		assertEquals("HttpClientWrapper cannot be null.", outputStreamCaptor.toString().trim());
	}
	
	@Test
	void testExecute_ForNoBills() throws IOException, InterruptedException, BadRequestException {
		Type debtType = new TypeToken<ArrayList<Debt>>(){}.getType();
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts", debtType)).thenReturn(Collections.emptyList());
		
		Type paymentPlanType = new TypeToken<ArrayList<PaymentPlan>>(){}.getType();
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans", paymentPlanType)).thenReturn(Collections.emptyList());
		
		Type paymentType = new TypeToken<ArrayList<Payment>>(){}.getType();
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments", paymentType)).thenReturn(Collections.emptyList());
		
		Operation main = new Operation(mockClientWrapper);
		main.execute();
		
		assertEquals("", outputStreamCaptor.toString().trim());
	}
	
	@Test
	void testExecute_ForOneBill() throws IOException, InterruptedException, BadRequestException {		
		Type debtType = new TypeToken<ArrayList<Debt>>(){}.getType();
		Debt dummyDebt = new Gson().fromJson("{\"amount\":102.50,\"id\":0}", Debt.class);
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts", debtType)).thenReturn(Arrays.asList(dummyDebt));
		
		Type paymentPlanType = new TypeToken<ArrayList<PaymentPlan>>(){}.getType();
		PaymentPlan dummyPaymentPlan = new Gson().fromJson("{\"amount_to_pay\":102.50,\"debt_id\":0,\"id\":0,\"installment_amount\":51.25,\"installment_frequency\":\"WEEKLY\",\"start_date\":\"2020-09-29\"}", PaymentPlan.class);
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans", paymentPlanType)).thenReturn(Arrays.asList(dummyPaymentPlan));
		
		Type paymentType = new TypeToken<ArrayList<Payment>>(){}.getType();
		Payment dummyPayment = new Gson().fromJson("{\"amount\":51.25,\"date\":\"2020-09-29\",\"payment_plan_id\":0}", Payment.class);
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments", paymentType)).thenReturn(Arrays.asList(dummyPayment));
		
		Operation main = new Operation(mockClientWrapper);
		main.execute();
		
		assertEquals("{\"id\":0,\"amount\":102.50,\"isInPaymentPlan\":true,\"remainingAmount\":51.25,\"nextPaymentDueDate\":\"2020-10-06T00:00:00.000Z\"}", outputStreamCaptor.toString()
			      .trim());
	}
	
	@Test
	void testExecute_ForTwoBills() throws IOException, InterruptedException, BadRequestException {	
		Type debtType = new TypeToken<ArrayList<Debt>>(){}.getType();
		Debt dummyDebt1 = new Gson().fromJson("{\"amount\":102.50,\"id\":0}", Debt.class);
		Debt dummyDebt2 = new Gson().fromJson("{\"amount\":204.50,\"id\":1}", Debt.class);
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts", debtType)).thenReturn(Arrays.asList(dummyDebt1, dummyDebt2));
		
		Type paymentPlanType = new TypeToken<ArrayList<PaymentPlan>>(){}.getType();
		PaymentPlan dummyPaymentPlan1 = new Gson().fromJson("{\"amount_to_pay\":102.50,\"debt_id\":0,\"id\":0,\"installment_amount\":51.25,\"installment_frequency\":\"WEEKLY\",\"start_date\":\"2020-09-29\"}", PaymentPlan.class);
		PaymentPlan dummyPaymentPlan2 = new Gson().fromJson("{\"amount_to_pay\":204.50,\"debt_id\":1,\"id\":1,\"installment_amount\":102.25,\"installment_frequency\":\"WEEKLY\",\"start_date\":\"2020-09-29\"}", PaymentPlan.class);
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans", paymentPlanType)).thenReturn(Arrays.asList(dummyPaymentPlan1, dummyPaymentPlan2));
		
		Type paymentType = new TypeToken<ArrayList<Payment>>(){}.getType();
		Payment dummyPayment1 = new Gson().fromJson("{\"amount\":51.25,\"date\":\"2020-09-29\",\"payment_plan_id\":0}", Payment.class);
		Payment dummyPayment2 = new Gson().fromJson("{\"amount\":102.25,\"date\":\"2020-09-29\",\"payment_plan_id\":1}", Payment.class);
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments", paymentType)).thenReturn(Arrays.asList(dummyPayment1, dummyPayment2));
		
		Operation main = new Operation(mockClientWrapper);
		main.execute();
		
		assertEquals("{\"id\":0,\"amount\":102.50,\"isInPaymentPlan\":true,\"remainingAmount\":51.25,\"nextPaymentDueDate\":\"2020-10-06T00:00:00.000Z\"}\r\n"
				+ "{\"id\":1,\"amount\":204.50,\"isInPaymentPlan\":true,\"remainingAmount\":102.25,\"nextPaymentDueDate\":\"2020-10-06T00:00:00.000Z\"}", 
				outputStreamCaptor.toString().trim());
	}
		
	@Test
	void testExecute_ForOneBill_WithoutAPaymentPlan() throws IOException, InterruptedException, BadRequestException {
		Type debtType = new TypeToken<ArrayList<Debt>>(){}.getType();
		Debt dummyDebt = new Gson().fromJson("{\"amount\":102.50,\"id\":0}", Debt.class);
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts", debtType)).thenReturn(Arrays.asList(dummyDebt));
		
		Type paymentPlanType = new TypeToken<ArrayList<PaymentPlan>>(){}.getType();
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans", paymentPlanType)).thenReturn(Collections.emptyList());
		
		Type paymentType = new TypeToken<ArrayList<Payment>>(){}.getType();
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments", paymentType)).thenReturn(Arrays.asList(Collections.emptyList()));
		
		Operation main = new Operation(mockClientWrapper);
		main.execute();
		
		assertEquals("{\"id\":0,\"amount\":102.50,\"isInPaymentPlan\":false,\"remainingAmount\":102.50,\"nextPaymentDueDate\":null}", 
				outputStreamCaptor.toString().trim());
	}
		
	@Test
	void testExecute_ForTwoBills_WithoutAPaymentPlan() throws IOException, InterruptedException, BadRequestException {
		Type debtType = new TypeToken<ArrayList<Debt>>(){}.getType();
		Debt dummyDebt1 = new Gson().fromJson("{\"amount\":102.50,\"id\":0}", Debt.class);
		Debt dummyDebt2 = new Gson().fromJson("{\"amount\":204.50,\"id\":1}", Debt.class);
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts", debtType)).thenReturn(Arrays.asList(dummyDebt1, dummyDebt2));
		
		Type paymentPlanType = new TypeToken<ArrayList<PaymentPlan>>(){}.getType();
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans", paymentPlanType)).thenReturn(Collections.emptyList());
		
		Type paymentType = new TypeToken<ArrayList<Payment>>(){}.getType();
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments", paymentType)).thenReturn(Collections.emptyList());
		
		Operation main = new Operation(mockClientWrapper);
		main.execute();
		
		assertEquals("{\"id\":0,\"amount\":102.50,\"isInPaymentPlan\":false,\"remainingAmount\":102.50,\"nextPaymentDueDate\":null}\r\n"
				+ "{\"id\":1,\"amount\":204.50,\"isInPaymentPlan\":false,\"remainingAmount\":204.50,\"nextPaymentDueDate\":null}", 
				outputStreamCaptor.toString().trim());
	}
		
	@Test
	void testExecute_ForTwoBills_OneWithAPaymentPlan_OneWithoutAPaymentPlan() throws IOException, InterruptedException, BadRequestException {
		Type debtType = new TypeToken<ArrayList<Debt>>(){}.getType();
		Debt dummyDebt1 = new Gson().fromJson("{\"amount\":102.50,\"id\":0}", Debt.class);
		Debt dummyDebt2 = new Gson().fromJson("{\"amount\":204.50,\"id\":1}", Debt.class);
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts", debtType)).thenReturn(Arrays.asList(dummyDebt1, dummyDebt2));
		
		Type paymentPlanType = new TypeToken<ArrayList<PaymentPlan>>(){}.getType();
		PaymentPlan dummyPaymentPlan = new Gson().fromJson("{\"amount_to_pay\":204.50,\"debt_id\":1,\"id\":1,\"installment_amount\":102.25,\"installment_frequency\":\"WEEKLY\",\"start_date\":\"2020-09-29\"}", PaymentPlan.class);
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans", paymentPlanType)).thenReturn(Arrays.asList(dummyPaymentPlan));
		
		Type paymentType = new TypeToken<ArrayList<Payment>>(){}.getType();
		Payment dummyPayment = new Gson().fromJson("{\"amount\":102.25,\"date\":\"2020-09-29\",\"payment_plan_id\":1}", Payment.class);
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments", paymentType)).thenReturn(Arrays.asList(dummyPayment));
		
		Operation main = new Operation(mockClientWrapper);
		main.execute();
		
		assertEquals("{\"id\":0,\"amount\":102.50,\"isInPaymentPlan\":false,\"remainingAmount\":102.50,\"nextPaymentDueDate\":null}\r\n"
				+ "{\"id\":1,\"amount\":204.50,\"isInPaymentPlan\":true,\"remainingAmount\":102.25,\"nextPaymentDueDate\":\"2020-10-06T00:00:00.000Z\"}", 
				outputStreamCaptor.toString().trim());
	}
	
	@Test
	void testExecute_ForWhenAnAnIOExceptionIsThrown() throws IOException, InterruptedException, BadRequestException {
		IOException exception = new IOException("Test Exception Message");
		
		Type debtType = new TypeToken<ArrayList<Debt>>(){}.getType();
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts", debtType)).thenThrow(exception);

		Operation main = new Operation(mockClientWrapper);
		main.execute();
		
		assertEquals("An IOException was thrown for the following reason: Test Exception Message", 
				outputStreamCaptor.toString().trim());
	}
		
	@Test
	void testExecute_ForWhenAnAnInterruptedExceptionIsThrown() throws IOException, InterruptedException, BadRequestException {
		InterruptedException exception = new InterruptedException("Test Exception Message");
		
		Type debtType = new TypeToken<ArrayList<Debt>>(){}.getType();
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts", debtType)).thenThrow(exception);

		Operation main = new Operation(mockClientWrapper);
		main.execute();
		
		assertEquals("An InterruptedException was thrown for the following reason: Test Exception Message", 
				outputStreamCaptor.toString().trim());
	}
		
	@Test
	void testExecute_ForWhenABadRequestExceptionIsThrown() throws IOException, InterruptedException, BadRequestException {
		BadRequestException exception = new BadRequestException("Test Exception Message");
		
		Type debtType = new TypeToken<ArrayList<Debt>>(){}.getType();
		when(mockClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts", debtType)).thenThrow(exception);

		Operation main = new Operation(mockClientWrapper);
		main.execute();
		
		assertEquals("A BadRequestException was thrown for the following reason: Test Exception Message", 
				outputStreamCaptor.toString().trim());
	}
}