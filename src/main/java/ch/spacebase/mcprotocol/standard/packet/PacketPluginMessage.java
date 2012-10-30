package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketPluginMessage extends Packet {

	public String channel;
	public byte data[];
	
	public PacketPluginMessage() {
	}
	
	public PacketPluginMessage(String channel, byte data[]) {
		this.channel = channel;
		this.data = data;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.channel = IOUtils.readString(in);
		this.data = new byte[in.readShort()];
		in.readFully(this.data);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		IOUtils.writeString(out, this.channel);
		out.writeShort(this.data.length);
		out.write(this.data);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 250;
	}
	
}
