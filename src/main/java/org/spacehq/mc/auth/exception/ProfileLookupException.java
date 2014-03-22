package org.spacehq.mc.auth.exception;

public class ProfileLookupException extends ProfileException {

	private static final long serialVersionUID = 1L;

	public ProfileLookupException() {
	}

	public ProfileLookupException(String message) {
		super(message);
	}

	public ProfileLookupException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProfileLookupException(Throwable cause) {
		super(cause);
	}

}
