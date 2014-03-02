package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

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
		this.ping = in.readVarInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.name);
		out.writeBoolean(this.online);
		out.writeVarInt(this.ping);
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
