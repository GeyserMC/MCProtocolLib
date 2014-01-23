package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.Position;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.opennbt.tag.CompoundTag;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerUpdateTileEntityPacket implements Packet {

	private Position position;
	private Type type;
	private CompoundTag nbt;
	
	@SuppressWarnings("unused")
	private ServerUpdateTileEntityPacket() {
	}
	
	public ServerUpdateTileEntityPacket(int breakerEntityId, Position position, Type type, CompoundTag nbt) {
		this.position = position;
		this.type = type;
		this.nbt = nbt;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public CompoundTag getNBT() {
		return this.nbt;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.position = NetUtil.readPosition(in);
		this.type = Type.values()[in.readUnsignedByte() - 1];
		this.nbt = NetUtil.readNBT(in);
	}

	@Override
	public void write(NetOutput out) throws IOException {
		NetUtil.writePosition(out, this.position);
		out.writeByte(this.type.ordinal() + 1);
		NetUtil.writeNBT(out, this.nbt);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

	public static enum Type {
		MOB_SPAWNER,
		COMMAND_BLOCK,
		BEACON,
		SKULL,
		FLOWER_POT;
	}
	
}
