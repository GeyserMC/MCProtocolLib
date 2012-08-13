package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketExplosion extends Packet {

	public double x;
	public double y;
	public double z;
	public float radius;
	public byte blocks[];
	public float unk1;
	public float unk2;
	public float unk3;
	
	public PacketExplosion() {
	}
	
	public PacketExplosion(double x, double y, double z, float radius, byte blocks[], float unk1, float unk2, float unk3) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
		this.blocks = blocks;
		this.unk1 = unk1;
		this.unk2 = unk2;
		this.unk3 = unk3;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.x = in.readDouble();
		this.y = in.readDouble();
		this.z = in.readDouble();
		this.radius = in.readFloat();
		this.blocks = new byte[in.readInt() * 3];
		in.readFully(this.blocks);
		this.unk1 = in.readFloat();
		this.unk2 = in.readFloat();
		this.unk3 = in.readFloat();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeDouble(this.x);
		out.writeDouble(this.y);
		out.writeDouble(this.z);
		out.writeFloat(this.radius);
		out.writeInt(this.blocks.length / 3);
		out.write(this.blocks);
		out.writeFloat(this.unk1);
		out.writeFloat(this.unk2);
		out.writeFloat(this.unk3);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 60;
	}
	
}
