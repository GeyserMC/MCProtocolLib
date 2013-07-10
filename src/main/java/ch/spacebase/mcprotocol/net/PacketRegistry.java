package ch.spacebase.mcprotocol.net;

import ch.spacebase.mcprotocol.packet.Packet;

public abstract class PacketRegistry {

	/**
	 * The packet registry array.
	 */
	@SuppressWarnings("unchecked")
	private final Class<? extends Packet> packets[] = new Class[256];

	/**
	 * Registers a packet.
	 * @param id Id of the packet.
	 * @param packet Class of the packet.
	 */
	public void registerPacket(int id, Class<? extends Packet> packet) {
		this.packets[id] = packet;
	}

	/**
	 * Gets a packet class from the given id.
	 * @param id Id of the packet.
	 * @return The packet class.
	 */
	public Class<? extends Packet> getPacket(int id) {
		try {
			return this.packets[id];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
}
