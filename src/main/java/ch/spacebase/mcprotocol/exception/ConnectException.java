package ch.spacebase.mcprotocol.exception;

/**
 * An exception thrown when an error occurs while connecting.
 */
public class ConnectException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new connect exception.
	 * @param message Message describing the exception.
	 */
	public ConnectException(String message) {
		super(message);
	}

	/**
	 * Creates a new connect exception.
	 * @param message Message describing the exception.
	 * @param e Cause of this exception
	 */
	public ConnectException(String message, Exception e) {
		super(message, e);
	}

}
