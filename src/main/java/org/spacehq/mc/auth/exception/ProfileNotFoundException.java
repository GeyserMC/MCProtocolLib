package org.spacehq.mc.auth.exception;

public class ProfileNotFoundException extends ProfileException {

	private static final long serialVersionUID = 1L;

	public ProfileNotFoundException() {
	}

	public ProfileNotFoundException(String message) {
		super(message);
	}

	public ProfileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProfileNotFoundException(Throwable cause) {
		super(cause);
	}
}
