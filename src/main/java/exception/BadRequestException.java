package exception;

/**
 * Exception for when a request is invalid.
 */
public class BadRequestException extends Exception{  

	private static final long serialVersionUID = 7816096282166048990L;

	/**
	 * Constructor to create an instance of this class.
	 * @param message for the exception. Can be null, empty or blank.
	 */
	public BadRequestException(String message){  
	  super(message);  
	 }  
}