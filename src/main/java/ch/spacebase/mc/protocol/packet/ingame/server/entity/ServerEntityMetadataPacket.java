package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.EntityMetadata;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerEntityMetadataPacket implements Packet {
	
	private int entityId;
	private EntityMetadata metadata[];
	
	@SuppressWarnings("unused")
	private ServerEntityMetadataPacket() {
	}
	
	public ServerEntityMetadataPacket(int entityId, EntityMetadata metadata[]) {
		this.entityId = entityId;
		this.metadata = metadata;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public EntityMetadata[] getMetadata() {
		return this.metadata;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.metadata = NetUtil.readEntityMetadata(in);
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		NetUtil.writeEntityMetadata(out, this.metadata);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
