package ch.spacebase.mc.protocol.packet.ingame.server;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerPlayerListEntryPacket implements Packet {
	
	private String name;
	private boolean online;
	private int ping;
	
	@SuppressWarnings("unused")
	private ServerPlayerListEntryPacket() {
	}
	
	public ServerPlayerListEntryPacket(String name, boolean online, int ping) {
		this.name = name;
		this.online = online;
		this.ping = ping;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean getOnline() {
		return this.online;
	}
	
	public int getPing() {
		return this.ping;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.name = in.readString();
		this.online = in.readBoolean();
		this.ping = in.readShort();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.name);
		out.writeBoolean(this.online);
		out.writeShort(this.ping);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
