package org.spacehq.mc.protocol.packet.login.client;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class LoginStartPacket implements Packet {

	private String username;

	@SuppressWarnings("unused")
	private LoginStartPacket() {
	}

	public LoginStartPacket(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void read(NetInput in) throws IOException {
		this.username = in.readString();
	}

	public void write(NetOutput out) throws IOException {
		out.writeString(this.username);
	}

	public boolean isPriority() {
		return true;
	}

}
