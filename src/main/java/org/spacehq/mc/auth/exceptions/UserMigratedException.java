package org.spacehq.mc.auth.exceptions;

public class UserMigratedException extends InvalidCredentialsException {

	private static final long serialVersionUID = 1L;
	
	public UserMigratedException() {
	}

	public UserMigratedException(String message) {
		super(message);
	}

	public UserMigratedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserMigratedException(Throwable cause) {
		super(cause);
	}
}
