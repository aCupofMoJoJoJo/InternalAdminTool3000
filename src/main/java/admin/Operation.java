package admin;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exception.BadRequestException;
import http.HttpClientWrapper;
import json.Debt;
import json.Payment;
import json.PaymentPlan;
import model.Bill;
import service.DebtCalculator;

/**
 * Operation class for InternalAdminTool3000. This class executes commands to retrieve, read, generate and print out bills.
 */
public class Operation {
    private HttpClientWrapper httpClientWrapper;
    
    /**
     * Constructor to create an instance of this class.
     * @param httpClientWrapper the wrapper for HttpClient. Cannot be null.
     */
    public Operation(HttpClientWrapper httpClientWrapper)
    {
    	if(httpClientWrapper == null)
    	{
    		System.out.println("HttpClientWrapper cannot be null.");
    	}
    	this.httpClientWrapper = httpClientWrapper;
    }
	
    /**
     * Executes the application.
     */
	public void execute()
	{
		try {
			retrieve();
		} catch (IOException exception) {
			System.out.println("An IOException was thrown for the following reason: " + exception.getMessage());
		} catch (InterruptedException exception) {
			System.out.println("An InterruptedException was thrown for the following reason: " + exception.getMessage());
		} catch (BadRequestException exception) {
			System.out.println("A BadRequestException was thrown for the following reason: " + exception.getMessage());
		}
	}
	
	/**
	 * Retrieves debt and payments and calls generate to produce a bill.
	 * 
     * @throws IOException if an I/O error occurs while the HttpClient is executing the request
     * @throws InterruptedException if the operation is interrupted while the HttpClient is executing the request
     * @throws BadRequestException if the request is invalid
	 */
	private void retrieve() throws IOException, InterruptedException, BadRequestException
	{		
		Type debtType = new TypeToken<ArrayList<Debt>>(){}.getType();
		List<Debt> debts = httpClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts", debtType);
		Type paymentPlanType = new TypeToken<ArrayList<PaymentPlan>>(){}.getType();
		List<PaymentPlan> paymentPlans = httpClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans", paymentPlanType);
		Type paymentType = new TypeToken<ArrayList<Payment>>(){}.getType();
		List<Payment> payments = httpClientWrapper.get("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments", paymentType);
									
		generate(debts, paymentPlans, payments);
	}
	
	/**
	 * Generates a bill list and calls print.
	 * 
	 * @param debts the list of outstanding debts.
	 * @param paymentPlans the list of payment plans for outstanding debt.
	 * @param payments the list of payments made towards payment plans for outstanding debts.
	 */
	private void generate(List<Debt> debts, List<PaymentPlan> paymentPlans, List<Payment> payments)
	{
		List<Bill> bills = new ArrayList<Bill>();
		
		for(Debt debt : debts)
		{			
			bills = generateBillList(debt, paymentPlans, payments, bills);
		}
		
		print(bills);
	}
	
	/**
	 * Produces a bill and adds it to the existing list.
	 * 
	 * @param debt the outstanding debt.
	 * @param paymentPlans the list of payment plans for outstanding debts.
	 * @param payments the list of payments made towards payment plans for outstanding debts.
	 * @param billsList the list of existing bills.
	 * 
	 * @return the list of bills with the newly created bill added to it.
	 */
	private static List<Bill> generateBillList(Debt debt, List<PaymentPlan> paymentPlans, List<Payment> payments, List<Bill> billsList)
	{
		List<Bill> bills = billsList;

		Optional<PaymentPlan> paymentPlan = paymentPlans.stream()
				.filter(plan -> plan.getDebtId() == debt.getId()).findFirst();
					
		if(paymentPlan.isPresent())
		{
			bills.add(produceBill(debt, paymentPlan.get(), payments));
		}
		else {
			bills.add(new Bill(debt.getId(), debt.getAmount(), false, debt.getAmount(), null));
		}
		return bills;
	}
	
	/**
	 * Produces a bill for an outstanding debt.
	 * 
	 * @param debt the outstanding debt.
	 * @param paymentPlan the payment plan for outstanding debt.
	 * @param payments the list of payments made towards a payment plan for an outstanding debt.
	 * 
	 * @return a bill for an outstanding debt.
	 */
	private static Bill produceBill(Debt debt, PaymentPlan paymentPlan, List<Payment> payments)
	{
		List<Payment> paymentList = payments.stream()
				.filter(payment -> payment.getPaymentPlanId() == paymentPlan.getId()).collect(Collectors.toList());
		
		DebtCalculator calculator = new DebtCalculator(debt, paymentPlan, paymentList);
		return calculator.calculate();
	}
	
	/**
	 * Prints out the bill for out the outstanding debt.
	 * 
	 * @param bills the list of bills to be printed out.
	 */
	private static void print(List<Bill> bills)
	{
		Gson gson = new GsonBuilder().serializeNulls().create();
		for (Bill bill : bills) {
			String jsonString = gson.toJson(bill);
			System.out.println(jsonString);
		}
	}
}