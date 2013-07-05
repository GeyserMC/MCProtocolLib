package ch.spacebase.mcprotocol.event;

import ch.spacebase.mcprotocol.packet.Packet;

/**
 * Called when a packet is received.
 */
public class PacketRecieveEvent extends ProtocolEvent<ProtocolListener> {

	/**
	 * The received packet.
	 */
	private Packet packet;

	/**
	 * Creates a new packet receive event instance.
	 * @param packet Packet being received.
	 */
	public PacketRecieveEvent(Packet packet) {
		this.packet = packet;
	}

	/**
	 * Gets the received packet.
	 * @return The received packet.
	 */
	public Packet getPacket() {
		return this.packet;
	}

	/**
	 * Gets and casts the received packet to the given type.
	 * @param clazz Class to cast to.
	 * @return The received packet, or null if it is not of that type.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Packet> T getPacket(Class<T> clazz) {
		try {
			return (T) this.packet;
		} catch (ClassCastException e) {
			return null;
		}
	}

	@Override
	public void call(ProtocolListener listener) {
		listener.onPacketReceive(this);
	}

}
