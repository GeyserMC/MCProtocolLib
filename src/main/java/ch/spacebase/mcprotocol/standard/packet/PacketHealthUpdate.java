package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketHealthUpdate extends Packet {

	public float health;
	public short food;
	public float saturation;

	public PacketHealthUpdate() {
	}

	public PacketHealthUpdate(float health, short food, float saturation) {
		this.health = health;
		this.food = food;
		this.saturation = saturation;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.health = in.readFloat();
		this.food = in.readShort();
		this.saturation = in.readFloat();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeFloat(this.health);
		out.writeShort(this.food);
		out.writeFloat(saturation);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 8;
	}

}
