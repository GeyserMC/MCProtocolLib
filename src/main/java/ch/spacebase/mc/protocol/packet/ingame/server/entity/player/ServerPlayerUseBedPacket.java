package ch.spacebase.mc.protocol.packet.ingame.server.entity.player;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.Position;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerPlayerUseBedPacket implements Packet {
	
	private int entityId;
	private Position position;
	
	@SuppressWarnings("unused")
	private ServerPlayerUseBedPacket() {
	}
	
	public ServerPlayerUseBedPacket(int entityId, Position position) {
		this.entityId = entityId;
		this.position = position;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Position getPosition() {
		return this.position;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.position = NetUtil.readPosition(in);
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		NetUtil.writePosition(out, this.position);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
