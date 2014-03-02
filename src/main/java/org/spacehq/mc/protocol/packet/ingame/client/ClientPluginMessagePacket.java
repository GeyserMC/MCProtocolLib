package org.spacehq.mc.protocol.packet.ingame.client;

import java.io.IOException;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class ClientPluginMessagePacket implements Packet {
	
	private String channel;
	private byte data[];
	
	@SuppressWarnings("unused")
	private ClientPluginMessagePacket() {
	}
	
	public ClientPluginMessagePacket(String channel, byte data[]) {
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
