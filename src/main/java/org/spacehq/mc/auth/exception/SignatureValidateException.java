package org.spacehq.mc.auth.exception;

public class SignatureValidateException extends PropertyException {

	private static final long serialVersionUID = 1L;

	public SignatureValidateException() {
	}

	public SignatureValidateException(String message) {
		super(message);
	}

	public SignatureValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	public SignatureValidateException(Throwable cause) {
		super(cause);
	}

}
