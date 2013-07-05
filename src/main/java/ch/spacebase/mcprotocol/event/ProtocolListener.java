package ch.spacebase.mcprotocol.event;

/**
 * A listener for general connection protocol events.
 */
public abstract class ProtocolListener {

	/**
	 * Called when a packet is received.
	 * @param event The called event.
	 */
	public abstract void onPacketReceive(PacketRecieveEvent event);
	
	/**
	 * Called when a packet is sent.
	 * @param event The called event.
	 */
	public abstract void onPacketSend(PacketSendEvent event);
	
	/**
	 * Called when a connection is disconnected.
	 * @param event The called event.
	 */
	public abstract void onDisconnect(DisconnectEvent event);

}
