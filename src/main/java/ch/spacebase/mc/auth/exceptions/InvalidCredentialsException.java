package ch.spacebase.mc.auth.exceptions;

import ch.spacebase.mc.auth.exceptions.AuthenticationException;

public class InvalidCredentialsException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	public InvalidCredentialsException() {
	}

	public InvalidCredentialsException(String message) {
		super(message);
	}

	public InvalidCredentialsException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCredentialsException(Throwable cause) {
		super(cause);
	}
}
