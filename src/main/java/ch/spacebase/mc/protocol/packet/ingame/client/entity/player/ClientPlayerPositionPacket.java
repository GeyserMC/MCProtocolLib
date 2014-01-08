package ch.spacebase.mc.protocol.packet.ingame.client.entity.player;

public class ClientPlayerPositionPacket extends ClientPlayerMovementPacket {

	protected ClientPlayerPositionPacket() {
		this.pos = true;
	}
	
	public ClientPlayerPositionPacket(boolean onGround, double x, double stance, double y, double z) {
		super(onGround);
		this.pos = true;
		this.x = x;
		this.stance = stance;
		this.y = y;
		this.z = z;
	}

}
