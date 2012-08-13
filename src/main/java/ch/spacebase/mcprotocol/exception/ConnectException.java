package ch.spacebase.mcprotocol.exception;

public class ConnectException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ConnectException(String message) {
		super(message);
	}
	
	public ConnectException(String message, Exception e) {
		super(message, e);
	}

}
