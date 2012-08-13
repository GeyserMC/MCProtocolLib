package ch.spacebase.mcprotocol.event;

public class LoginEvent extends ProtocolEvent {

	private String reason = "Login denied.";
	private boolean disallow = false;
	
	public String getReason() {
		return this.reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public boolean disallow() {
		return this.disallow;
	}
	
	public void setDisallow(boolean disallow) {
		this.disallow = disallow;
	}
	
	@Override
	public void call(ProtocolListener listener) {
		listener.onLogin(this);
	}

}
