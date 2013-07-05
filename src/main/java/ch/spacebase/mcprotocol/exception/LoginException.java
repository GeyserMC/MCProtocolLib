package ch.spacebase.mcprotocol.exception;

/**
 * An exception thrown when there is an error logging in.
 */
public class LoginException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new login exception.
	 * @param message Message describing the exception.
	 */
	public LoginException(String message) {
		super(message);
	}

	/**
	 * Creates a new login exception.
	 * @param message Message describing the exception.
	 * @param e Cause of this exception
	 */
	public LoginException(String message, Exception e) {
		super(message, e);
	}

}
