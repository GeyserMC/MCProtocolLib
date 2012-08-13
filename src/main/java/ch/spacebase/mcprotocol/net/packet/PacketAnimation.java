package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketAnimation extends Packet {

	public int entityId;
	public byte animation;
	
	public PacketAnimation() {
	}
	
	public PacketAnimation(int entityId, byte animation) {
		this.entityId = entityId;
		this.animation = animation;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.animation = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.animation);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 18;
	}
	
}
