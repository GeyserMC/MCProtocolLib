package ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.Position;
import ch.spacebase.mc.protocol.data.game.values.Art;
import ch.spacebase.mc.protocol.data.game.values.HangingDirection;
import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerSpawnPaintingPacket implements Packet {
	
	private int entityId;
	private Art art;
	private Position position;
	private HangingDirection direction;
	
	@SuppressWarnings("unused")
	private ServerSpawnPaintingPacket() {
	}
	
	public ServerSpawnPaintingPacket(int entityId, Art art, Position position, HangingDirection direction) {
		this.entityId = entityId;
		this.art = art;
		this.position = position;
		this.direction = direction;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Art getArt() {
		return this.art;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public HangingDirection getDirection() {
		return this.direction;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.art = MagicValues.key(Art.class, in.readString());
		this.position = NetUtil.readPosition(in);
		this.direction = MagicValues.key(HangingDirection.class, in.readUnsignedByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeString(MagicValues.value(String.class, this.art));
		NetUtil.writePosition(out, this.position);
		out.writeByte(MagicValues.value(Integer.class, this.direction));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
