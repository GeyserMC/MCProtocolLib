package ch.spacebase.mc.protocol.packet.ingame.client.entity.player;

public class ClientPlayerPositionPacket extends ClientPlayerMovementPacket {

	protected ClientPlayerPositionPacket() {
		this.pos = true;
	}
	
	public ClientPlayerPositionPacket(boolean onGround, double x, double feetY, double headY, double z) {
		super(onGround);
		this.pos = true;
		this.x = x;
		this.feetY = feetY;
		this.headY = headY;
		this.z = z;
	}

}
