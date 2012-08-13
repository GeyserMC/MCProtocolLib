package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketOpenWindow extends Packet {

	public byte id;
	public byte type;
	public String name;
	public byte slots;
	
	public PacketOpenWindow() {
	}
	
	public PacketOpenWindow(byte id, byte type, String name, byte slots) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.slots = slots;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.id = in.readByte();
		this.type = in.readByte();
		this.name = IOUtils.readString(in);
		this.slots = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(this.id);
		out.writeByte(this.type);
		IOUtils.writeString(out, this.name);
		out.writeByte(this.slots);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 100;
	}
	
}
