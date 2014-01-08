package ch.spacebase.mc.protocol.packet.ingame.server.entity;

public class ServerEntityPositionPacket extends ServerEntityMovementPacket {

	protected ServerEntityPositionPacket() {
		this.pos = true;
	}
	
	public ServerEntityPositionPacket(int entityId, double moveX, double moveY, double moveZ) {
		super(entityId);
		this.pos = true;
		this.moveX = moveX;
		this.moveY = moveY;
		this.moveZ = moveZ;
	}

}
