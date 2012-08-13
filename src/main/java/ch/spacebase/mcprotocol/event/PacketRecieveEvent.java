package ch.spacebase.mcprotocol.event;

import ch.spacebase.mcprotocol.net.packet.Packet;

public class PacketRecieveEvent extends ProtocolEvent {

	private Packet packet;
	
	public PacketRecieveEvent(Packet packet) {
		this.packet = packet;
	}
	
	public Packet getPacket() {
		return this.packet;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Packet> T getPacket(Class<T> clazz) {
		try {
			return (T) this.packet;
		} catch(ClassCastException e) {
			return null;
		}
	}
	
 	@Override
	public void call(ProtocolListener listener) {
		listener.onPacketRecieve(this);
	}
	
}
