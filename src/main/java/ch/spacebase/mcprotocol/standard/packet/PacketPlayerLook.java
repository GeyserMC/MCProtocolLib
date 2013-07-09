package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketPlayerLook extends Packet {

	public float yaw;
	public float pitch;
	public boolean grounded;

	public PacketPlayerLook() {
	}

	public PacketPlayerLook(float yaw, float pitch, boolean grounded) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.grounded = grounded;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.yaw = in.readFloat();
		this.pitch = in.readFloat();
		this.grounded = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
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
		return 12;
	}

}
