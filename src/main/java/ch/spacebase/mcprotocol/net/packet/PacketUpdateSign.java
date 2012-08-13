package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketUpdateSign extends Packet {

	public int x;
	public short y;
	public int z;
	public String lines[];
	
	public PacketUpdateSign() {
	}
	
	public PacketUpdateSign(int x, short y, int z, String lines[]) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.lines = lines;
		if(this.lines == null || this.lines.length != 4) throw new IllegalArgumentException("Line array size must be 4!");
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.x = in.readInt();
		this.y = in.readShort();
		this.z = in.readInt();
		this.lines = new String[4];
		this.lines[0] = IOUtils.readString(in);
		this.lines[1] = IOUtils.readString(in);
		this.lines[2] = IOUtils.readString(in);
		this.lines[3] = IOUtils.readString(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.x);
		out.writeShort(this.y);
		out.writeInt(this.z);
		IOUtils.writeString(out, this.lines[0]);
		IOUtils.writeString(out, this.lines[1]);
		IOUtils.writeString(out, this.lines[2]);
		IOUtils.writeString(out, this.lines[3]);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 130;
	}
	
}
