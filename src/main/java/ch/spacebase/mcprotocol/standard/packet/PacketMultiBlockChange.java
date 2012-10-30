package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketMultiBlockChange extends Packet {

	public int x;
	public int z;
	public int records;
	public byte data[];
	
	public PacketMultiBlockChange() {
	}
	
	public PacketMultiBlockChange(int x, int z, int records, byte data[]) {
		this.x = x;
		this.z = z;
		this.records = records;
		this.data = data;
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		this.x = in.readInt();
		this.z = in.readInt();
		this.records = in.readShort() & 0xffff;
		int size = in.readInt();
		if(size > 0) {
			this.data = new byte[size];
			in.readFully(this.data);
		}
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.x);
		out.writeInt(this.z);
		out.writeShort((short) this.records);
		if(this.data != null) {
			out.writeInt(this.data.length);
			out.write(this.data);
		} else {
			out.writeInt(0);
		}
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 52;
	}
	
}
