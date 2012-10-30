package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketHealthUpdate extends Packet {

	public short health;
	public short food;
	public float saturation;
	
	public PacketHealthUpdate() {
	}
	
	public PacketHealthUpdate(short health, short food, float saturation) {
		this.health = health;
		this.food = food;
		this.saturation = saturation;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.health = in.readShort();
		this.food = in.readShort();
		this.saturation = in.readFloat();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeShort(this.health);
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
