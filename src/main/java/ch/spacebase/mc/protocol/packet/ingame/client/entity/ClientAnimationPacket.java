package ch.spacebase.mc.protocol.packet.ingame.client.entity;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientAnimationPacket implements Packet {
	
	private int entityId;
	private Animation animation;
	
	@SuppressWarnings("unused")
	private ClientAnimationPacket() {
	}
	
	public ClientAnimationPacket(int entityId, Animation animation) {
		this.entityId = entityId;
		this.animation = animation;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Animation getAnimation() {
		return this.animation;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.animation = Animation.values()[in.readByte() - 1];
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.animation.ordinal() + 1);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Animation {
		SWING_ARM;
	}

}
