package ch.spacebase.mc.protocol.packet.ingame.server.entity;

public class ServerEntityRotationPacket extends ServerEntityMovementPacket {

	protected ServerEntityRotationPacket() {
		this.rot = true;
	}
	
	public ServerEntityRotationPacket(int entityId, float yaw, float pitch) {
		super(entityId);
		this.rot = true;
		this.yaw = yaw;
		this.pitch = pitch;
	}

}
