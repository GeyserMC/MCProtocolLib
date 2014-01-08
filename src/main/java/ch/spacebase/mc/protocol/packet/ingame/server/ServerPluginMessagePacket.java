package ch.spacebase.mc.protocol.packet.ingame.server;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerPluginMessagePacket implements Packet {
	
	private String channel;
	private byte data[];
	
	@SuppressWarnings("unused")
	private ServerPluginMessagePacket() {
	}
	
	public ServerPluginMessagePacket(String channel, byte data[]) {
		this.channel = channel;
		this.data = data;
	}
	
	public String getChannel() {
		return this.channel;
	}
	
	public byte[] getData() {
		return this.data;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.channel = in.readString();
		this.data = in.readBytes(in.readShort());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.channel);
		out.writeShort(this.data.length);
		out.writeBytes(this.data);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
