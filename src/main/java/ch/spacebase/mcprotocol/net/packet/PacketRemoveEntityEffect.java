package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketRemoveEntityEffect extends Packet {

	public int entityId;
	public byte effect;
	
	public PacketRemoveEntityEffect() {
	}
	
	public PacketRemoveEntityEffect(int entityId, byte effect) {
		this.entityId = entityId;
		this.effect = effect;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.effect = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.effect);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 42;
	}
	
}
