package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerEntityHeadLookPacket implements Packet {

	protected int entityId;
	protected float headYaw;

	@SuppressWarnings("unused")
	private ServerEntityHeadLookPacket() {
	}

	public ServerEntityHeadLookPacket(int entityId, float headYaw) {
		this.entityId = entityId;
		this.headYaw = headYaw;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public float getHeadYaw() {
		return this.headYaw;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.headYaw = in.readByte() * 360 / 256f;
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte((byte) (this.headYaw * 256 / 360));
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
