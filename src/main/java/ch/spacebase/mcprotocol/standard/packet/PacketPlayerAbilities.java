package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketPlayerAbilities extends Packet {

	public byte flags;
	public byte flySpeed;
	public byte walkSpeed;

	public PacketPlayerAbilities() {
	}

	public PacketPlayerAbilities(byte flags, byte flySpeed, byte walkSpeed) {
		this.flags = flags;
		this.flySpeed = flySpeed;
		this.walkSpeed = walkSpeed;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.flags = in.readByte();
		this.flySpeed = in.readByte();
		this.walkSpeed = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(this.flags);
		out.writeByte(this.flySpeed);
		out.writeByte(this.walkSpeed);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 202;
	}

}
