package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.Position;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerBlockBreakAnimPacket implements Packet {

	private int breakerEntityId;
	private Position position;
	private Stage stage;
	
	@SuppressWarnings("unused")
	private ServerBlockBreakAnimPacket() {
	}
	
	public ServerBlockBreakAnimPacket(int breakerEntityId, Position position, Stage stage) {
		this.breakerEntityId = breakerEntityId;
		this.position = position;
		this.stage = stage;
	}
	
	public int getBreakerEntityId() {
		return this.breakerEntityId;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public Stage getStage() {
		return this.stage;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.breakerEntityId = in.readVarInt();
		this.position = NetUtil.readPosition(in);
		try {
			this.stage = Stage.values()[in.readUnsignedByte()];
		} catch(ArrayIndexOutOfBoundsException e) {
			this.stage = Stage.RESET;
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.breakerEntityId);
		NetUtil.writePosition(out, this.position);
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
