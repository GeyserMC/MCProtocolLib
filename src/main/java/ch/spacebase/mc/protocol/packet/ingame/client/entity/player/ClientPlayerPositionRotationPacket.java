package ch.spacebase.mc.protocol.packet.ingame.client.entity.player;

public class ClientPlayerPositionRotationPacket extends ClientPlayerMovementPacket {

	public ClientPlayerPositionRotationPacket() {
		this.pos = true;
		this.rot = true;
	}
	
	public ClientPlayerPositionRotationPacket(boolean onGround, double x, double stance, double y, double z, float yaw, float pitch) {
		super(onGround);
		this.pos = true;
		this.rot = true;
		this.x = x;
		this.stance = stance;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

}
