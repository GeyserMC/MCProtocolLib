package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketSpawnParticle extends Packet {

	public String particle;
	public float x;
	public float y;
	public float z;
	public float offsetX;
	public float offsetY;
	public float offsetZ;
	public float speed;
	public int count;

	public PacketSpawnParticle() {
	}

	public PacketSpawnParticle(String particle, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float speed, int count) {
		this.particle = particle;
		this.x = x;
		this.y = y;
		this.z = z;
		this.speed = speed;
		this.count = count;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.particle = in.readString();
		this.x = in.readFloat();
		this.y = in.readFloat();
		this.z = in.readFloat();
		this.offsetX = in.readFloat();
		this.offsetY = in.readFloat();
		this.offsetZ = in.readFloat();
		this.speed = in.readFloat();
		this.count = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.particle);
		out.writeFloat(this.x);
		out.writeFloat(this.y);
		out.writeFloat(this.z);
		out.writeFloat(this.offsetX);
		out.writeFloat(this.offsetY);
		out.writeFloat(this.offsetZ);
		out.writeFloat(this.speed);
		out.writeInt(this.count);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 63;
	}

}
