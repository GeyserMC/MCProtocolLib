package ch.spacebase.mc.auth.exception;

public class TextureDecodeException extends PropertyException {

	private static final long serialVersionUID = 1L;

	public TextureDecodeException() {
	}

	public TextureDecodeException(String message) {
		super(message);
	}

	public TextureDecodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public TextureDecodeException(Throwable cause) {
		super(cause);
	}

}
