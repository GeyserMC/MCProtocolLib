package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketSetExperience extends Packet {

	public float experienceBar;
	public short level;
	public short experience;

	public PacketSetExperience() {
	}

	public PacketSetExperience(float experienceBar, short level, short experience) {
		this.experienceBar = experienceBar;
		this.level = level;
		this.experience = experience;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.experienceBar = in.readFloat();
		this.level = in.readShort();
		this.experience = in.readShort();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeFloat(this.experienceBar);
		out.writeShort(this.level);
		out.writeShort(this.experience);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 43;
	}

}
