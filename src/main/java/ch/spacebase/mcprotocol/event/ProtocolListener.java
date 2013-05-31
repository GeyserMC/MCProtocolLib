package ch.spacebase.mcprotocol.event;

public abstract class ProtocolListener {

	public abstract void onPacketRecieve(PacketReceiveEvent event);
	
	public abstract void onPacketSend(PacketSendEvent event);
	
	public abstract void onDisconnect(DisconnectEvent event);

}
