package ch.spacebase.mcprotocol.event;

public abstract class ProtocolEvent {

	public abstract void call(ProtocolListener listener);

}
