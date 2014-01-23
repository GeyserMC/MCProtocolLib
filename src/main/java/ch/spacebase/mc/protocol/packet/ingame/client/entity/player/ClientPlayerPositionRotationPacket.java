package ch.spacebase.mc.protocol.packet.ingame.client.entity.player;

public class ClientPlayerPositionRotationPacket extends ClientPlayerMovementPacket {

	protected ClientPlayerPositionRotationPacket() {
		this.pos = true;
		this.rot = true;
	}
	
	public ClientPlayerPositionRotationPacket(boolean onGround, double x, double feetY, double headY, double z, float yaw, float pitch) {
		super(onGround);
		this.pos = true;
		this.rot = true;
		this.x = x;
		this.feetY = feetY;
		this.headY = headY;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

}
