package ch.spacebase.mc.auth.exception;

public class PropertyException extends Exception {

	private static final long serialVersionUID = 1L;

	public PropertyException() {
	}

	public PropertyException(String message) {
		super(message);
	}

	public PropertyException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyException(Throwable cause) {
		super(cause);
	}

}
