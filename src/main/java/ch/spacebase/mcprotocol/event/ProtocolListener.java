package ch.spacebase.mcprotocol.event;

public abstract class ProtocolListener {
	
	public abstract void onPacketRecieve(PacketRecieveEvent event);

	public abstract void onLogin(LoginEvent event);

	public abstract void onLoginFinish(LoginFinishEvent event);
	
}
