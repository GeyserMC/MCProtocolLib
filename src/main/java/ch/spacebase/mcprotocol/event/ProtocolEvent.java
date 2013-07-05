package ch.spacebase.mcprotocol.event;

/**
 * An event relating to protocol.
 * @param <T> The type of listener used to listen to this event.
 */
public abstract class ProtocolEvent<T> {

	public abstract void call(T listener);

}
