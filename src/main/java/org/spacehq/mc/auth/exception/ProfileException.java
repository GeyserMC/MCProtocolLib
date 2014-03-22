package org.spacehq.mc.auth.exception;

public class ProfileException extends Exception {

	private static final long serialVersionUID = 1L;

	public ProfileException() {
	}

	public ProfileException(String message) {
		super(message);
	}

	public ProfileException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProfileException(Throwable cause) {
		super(cause);
	}

}
