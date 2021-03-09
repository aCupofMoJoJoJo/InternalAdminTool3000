package http;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.Gson;

import exception.BadRequestException;

/**
 * Wrapper class for the HttpClient's request methods.
 */
public class HttpClientWrapper {
    
    HttpClient client;
    
    /**
     * Constructor to create an instance of this class.
     * 
     * @param client the httpClient used to make HTTP requests. Cannot be null.
     */
    public HttpClientWrapper(HttpClient client) {
    	if(client == null)
    	{
    		System.out.println("HttpClient cannot be null.");
    	}
    	this.client = client;
    }
    
    /**
     * Executes a GET Request for the given URI and returns the object of the desire type.
     * 
     * @param <T> The desire object type. 
     * @param uri the URI to be executed. Cannot be null, empty or blank.
     * @param type the desire type used by GSON to deserializes the JSON into an object of the specified type
     * 
     * @return a list of objects of the specified typed. Will not be null but may be empty.
     * 
     * @throws IOException if an I/O error occurs while the HttpClient is executing the request
     * @throws InterruptedException if the operation is interrupted while the HttpClient is executing the request
     * @throws BadRequestException if the request is invalid
     */
	public <T> List<T> get(String uri, Type type) throws IOException, InterruptedException, BadRequestException
	{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request,
        	    HttpResponse.BodyHandlers.ofString()); 
               
        List<T> responseObject;
        if(response.statusCode() == 200)
        {
        	responseObject = new Gson().fromJson(response.body(), type);
        }
        else
        {
        	throw new BadRequestException("The following request failed: " + request.toString());
        }
                
		return responseObject;
	}
}