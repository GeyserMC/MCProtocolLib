package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerCollectItemPacket implements Packet {
	
	private int collectedEntityId;
	private int collectorEntityId;
	
	@SuppressWarnings("unused")
	private ServerCollectItemPacket() {
	}
	
	public ServerCollectItemPacket(int collectedEntityId, int collectorEntityId) {
		this.collectedEntityId = collectedEntityId;
		this.collectorEntityId = collectorEntityId;
	}
	
	public int getCollectedEntityId() {
		return this.collectedEntityId;
	}
	
	public int getCollectorEntityId() {
		return this.collectorEntityId;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.collectedEntityId = in.readVarInt();
		this.collectorEntityId = in.readVarInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.collectedEntityId);
		out.writeVarInt(this.collectorEntityId);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
