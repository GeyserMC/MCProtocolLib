package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerBlockBreakAnimPacket implements Packet {

	private int breakerEntityId;
	private int x;
	private int y;
	private int z;
	private Stage stage;
	
	@SuppressWarnings("unused")
	private ServerBlockBreakAnimPacket() {
	}
	
	public ServerBlockBreakAnimPacket(int breakerEntityId, int x, int y, int z, Stage stage) {
		this.breakerEntityId = breakerEntityId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.stage = stage;
	}
	
	public int getBreakerEntityId() {
		return this.breakerEntityId;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public Stage getStage() {
		return this.stage;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.breakerEntityId = in.readVarInt();
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
		try {
			this.stage = Stage.values()[in.readUnsignedByte()];
		} catch(ArrayIndexOutOfBoundsException e) {
			this.stage = Stage.RESET;
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.breakerEntityId);
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
		out.writeByte(this.stage == Stage.RESET ? -1 : this.stage.ordinal());
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

	public static enum Stage {
		STAGE_1,
		STAGE_2,
		STAGE_3,
		STAGE_4,
		STAGE_5,
		STAGE_6,
		STAGE_7,
		STAGE_8,
		STAGE_9,
		STAGE_10,
		RESET;
	}
	
}
