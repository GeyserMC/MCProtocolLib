package org.spacehq.mc.auth.exception;

public class PropertyDeserializeException extends PropertyException {

	private static final long serialVersionUID = 1L;

	public PropertyDeserializeException() {
	}

	public PropertyDeserializeException(String message) {
		super(message);
	}

	public PropertyDeserializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyDeserializeException(Throwable cause) {
		super(cause);
	}

}
