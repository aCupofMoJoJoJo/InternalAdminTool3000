package admin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import exception.BadRequestException;

/**
 * Integration test for Internal Admin Tool 3000.
 */
@RunWith(JUnitPlatform.class)
class AppIntegrationTest {	
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	
	@BeforeEach
	void setUp() {
		System.setOut(new PrintStream(outputStreamCaptor));
		DateTimeUtils.setCurrentMillisFixed(new DateTime().getMillis());
	}
	
	@AfterEach
	void tearDown() {
		DateTimeUtils.setCurrentMillisSystem();
	}
	
	@Test
	void testAdminTool() throws IOException, InterruptedException, BadRequestException {
		App.main(null);
		
		DateTime currentDateTime = DateTime.now();
		
		assertEquals("{\"id\":0,\"amount\":123.46,\"isInPaymentPlan\":true,\"remainingAmount\":0.00,\"nextPaymentDueDate\":null}\r\n"
				+ "{\"id\":1,\"amount\":100.00,\"isInPaymentPlan\":true,\"remainingAmount\":50.00,\"nextPaymentDueDate\":\"2020-08-15T00:00:00.000Z\"}\r\n"
				+ "{\"id\":2,\"amount\":4920.34,\"isInPaymentPlan\":true,\"remainingAmount\":607.67,\"nextPaymentDueDate\":\"" + currentDateTime.toString() +"\"}\r\n"	
				+ "{\"id\":3,\"amount\":12938.00,\"isInPaymentPlan\":true,\"remainingAmount\":622.41,\"nextPaymentDueDate\":\"2020-08-22T00:00:00.000Z\"}\r\n"				
				+ "{\"id\":4,\"amount\":9238.02,\"isInPaymentPlan\":false,\"remainingAmount\":9238.02,\"nextPaymentDueDate\":null}", 
				outputStreamCaptor.toString().trim());
	}
}