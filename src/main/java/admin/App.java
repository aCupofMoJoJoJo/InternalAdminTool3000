package admin;

import java.net.http.HttpClient;

import http.HttpClientWrapper;

/**
 * Application class for InternalAdminTool3000. This class is the main driver of the application and calls the operation class.
 */
public class App {

	/**
	 * Main method for InternalAdmintTool3000
	 * @param args the arguments used for application. Can be null, empty or blank.
	 */
	public static void main(String[] args) {
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpClientWrapper httpClientWrapper = new HttpClientWrapper(httpClient);
		Operation main = new Operation(httpClientWrapper);
		main.execute();
	}
}