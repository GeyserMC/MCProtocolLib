package org.spacehq.mc.protocol.packet.ingame.client.player;

import java.io.IOException;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class ClientPlayerAnimationPacket implements Packet {
	
	private int entityId;
	private Animation animation;
	
	@SuppressWarnings("unused")
	private ClientPlayerAnimationPacket() {
	}
	
	public ClientPlayerAnimationPacket(int entityId, Animation animation) {
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
