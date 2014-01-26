package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.Position;
import ch.spacebase.mc.protocol.data.game.values.BlockBreakStage;
import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerBlockBreakAnimPacket implements Packet {

	private int breakerEntityId;
	private Position position;
	private BlockBreakStage stage;
	
	@SuppressWarnings("unused")
	private ServerBlockBreakAnimPacket() {
	}
	
	public ServerBlockBreakAnimPacket(int breakerEntityId, Position position, BlockBreakStage stage) {
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
	
	public BlockBreakStage getStage() {
		return this.stage;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.breakerEntityId = in.readVarInt();
		this.position = NetUtil.readPosition(in);
		this.stage = MagicValues.key(BlockBreakStage.class, in.readUnsignedByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.breakerEntityId);
		NetUtil.writePosition(out, this.position);
		out.writeByte(MagicValues.value(Integer.class, this.stage));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
}
