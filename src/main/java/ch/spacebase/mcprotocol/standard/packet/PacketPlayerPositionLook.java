package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketPlayerPositionLook extends Packet {

	public double x;
	public double y;
	public double stance;
	public double z;
	public float yaw;
	public float pitch;
	public boolean grounded;

	public PacketPlayerPositionLook() {
	}

	public PacketPlayerPositionLook(double x, double y, double stance, double z, float yaw, float pitch, boolean grounded) {
		this.x = x;
		this.y = y;
		this.stance = stance;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.grounded = grounded;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.x = in.readDouble();
		this.y = in.readDouble();
		this.stance = in.readDouble();
		this.z = in.readDouble();
		this.yaw = in.readFloat();
		this.pitch = in.readFloat();
		this.grounded = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeDouble(this.x);
		out.writeDouble(this.y);
		out.writeDouble(this.stance);
		out.writeDouble(this.z);
		out.writeFloat(this.yaw);
		out.writeFloat(this.pitch);
		out.writeBoolean(this.grounded);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 13;
	}

}
