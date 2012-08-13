package ch.spacebase.mcprotocol.event;

public class LoginFinishEvent extends ProtocolEvent {

	@Override
	public void call(ProtocolListener listener) {
		listener.onLoginFinish(this);
	}

}
