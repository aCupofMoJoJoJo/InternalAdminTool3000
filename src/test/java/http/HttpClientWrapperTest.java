package http;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import json.Debt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import admin.Operation;
import exception.BadRequestException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for HttpClientWrapper.
 */
@RunWith(JUnitPlatform.class)
class HttpClientWrapperTest {
	
	private HttpClientWrapper httpClientWrapper;
	
	private HttpClient mockClient = mock(HttpClient.class);
	
	@SuppressWarnings("unchecked")
	private HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
	
	private Type debtType;
	
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	
	@BeforeEach
	void setUp() throws IOException, InterruptedException {
		httpClientWrapper = new HttpClientWrapper(this.mockClient);
		when(mockClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
			.thenReturn(mockHttpResponse);
		when(mockHttpResponse.statusCode()).thenReturn(200);
		this.debtType = new TypeToken<ArrayList<Debt>>(){}.getType();
		System.setOut(new PrintStream(outputStreamCaptor));
	}
		
	@Test
	void testConstructor_IsInstanceOfHttpClientWrapper() {
		HttpClientWrapper httpClientWrapper = new HttpClientWrapper(HttpClient.newHttpClient());
		
		assertTrue(httpClientWrapper instanceof HttpClientWrapper);
	}
	
	@Test
	void testConstructor_WhenNullIsPassed() {
		new HttpClientWrapper(null);
		
		assertEquals("HttpClient cannot be null.", outputStreamCaptor.toString().trim());
	}

	@Test
	void testGet_ReturnsNoItems() throws IOException, InterruptedException, BadRequestException {								
		when(this.mockHttpResponse.body()).thenReturn("[]");
		
		List<Debt> actualDebts = httpClientWrapper.get("https://TestURL", this.debtType);
		
		assertEquals(0, actualDebts.size());		
	}
	
	@Test
	void testGet_ReturnsOneItem() throws IOException, InterruptedException, BadRequestException {		
		Debt expectDebt = new Gson().fromJson("{\"amount\":123.46,\"id\":0}", Debt.class);
		when(this.mockHttpResponse.body()).thenReturn("[{\"amount\":123.46,\"id\":0}]");
				
		List<Debt> actualDebts = httpClientWrapper.get("https://TestURL", this.debtType);
		
		assertEquals(1, actualDebts.size());
		
		Debt actualDebt = actualDebts.get(0);
		
		assertEquals(expectDebt.getAmount(), actualDebt.getAmount());
		assertEquals(expectDebt.getId(), actualDebt.getId());
	}
	
	@Test
	void testGet_ReturnsTwoItems() throws IOException, InterruptedException, BadRequestException {
		Debt expectDebt1 = new Gson().fromJson("{\"amount\":123.46,\"id\":0}", Debt.class);
		Debt expectDebt2 = new Gson().fromJson("{\"amount\":100,\"id\":1}", Debt.class);

		when(this.mockHttpResponse.body()).thenReturn("[{\"amount\":123.46,\"id\":0},{\"amount\":100,\"id\":1}]");
		
		List<Debt> actualDebts = httpClientWrapper.get("https://TestURL", this.debtType);
		
		assertEquals(2, actualDebts.size());
		
		Debt actualDebt1 = actualDebts.get(0);
		
		assertEquals(expectDebt1.getAmount(), actualDebt1.getAmount());
		assertEquals(expectDebt1.getId(), actualDebt1.getId());
		
		Debt actualDebt2 = actualDebts.get(1);
		
		assertEquals(expectDebt2.getAmount(), actualDebt2.getAmount());
		assertEquals(expectDebt2.getId(), actualDebt2.getId());
	}
	
	@Test
	void testGet_ThrowsBadRequestException() throws IOException, InterruptedException {				
		when(this.mockHttpResponse.statusCode()).thenReturn(400);

		Exception expectedException = Assertions.assertThrows(BadRequestException.class, () -> {
			httpClientWrapper.get("https://TestURL", this.debtType);
		});
		
		assertEquals("The following request failed: https://TestURL GET", expectedException.getMessage());
	}
	
	@Test
	void testGet_ClientSendThrowsIOException() throws IOException, InterruptedException {		
		when(this.mockClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
			.thenThrow(IOException.class);
		
		Assertions.assertThrows(IOException.class, () -> {
			httpClientWrapper.get("https://TestURL", this.debtType);
		});		
	}
	
	@Test
	void testGet_ClientSendThrowsInterruptedException() throws IOException, InterruptedException {
		when(this.mockClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
			.thenThrow(InterruptedException.class);
		
		Assertions.assertThrows(InterruptedException.class, () -> {
			httpClientWrapper.get("https://TestURL", this.debtType);
		});		
	}
}