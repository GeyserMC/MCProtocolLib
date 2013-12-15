package ch.spacebase.mc.auth.exceptions;

import ch.spacebase.mc.auth.exceptions.AuthenticationException;

public class AuthenticationUnavailableException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	public AuthenticationUnavailableException() {
	}

	public AuthenticationUnavailableException(String message) {
		super(message);
	}

	public AuthenticationUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationUnavailableException(Throwable cause) {
		super(cause);
	}
}
