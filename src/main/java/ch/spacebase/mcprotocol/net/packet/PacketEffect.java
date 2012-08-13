package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketEffect extends Packet {

	public int effectId;
	public int x;
	public byte y;
	public int z;
	public int data;
	
	public PacketEffect() {
	}
	
	public PacketEffect(int effectId, int x, byte y, int z, int data) {
		this.effectId = effectId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.effectId = in.readInt();
		this.x = in.readInt();
		this.y = in.readByte();
		this.z = in.readInt();
		this.data = in.readInt();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.effectId);
		out.writeInt(this.x);
		out.writeByte(this.y);
		out.writeInt(this.z);
		out.writeInt(this.data);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 61;
	}
	
}
