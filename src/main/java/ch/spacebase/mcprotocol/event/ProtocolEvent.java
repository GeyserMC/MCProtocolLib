package ch.spacebase.mcprotocol.event;

public abstract class ProtocolEvent<T> {

	public abstract void call(T listener);

}
