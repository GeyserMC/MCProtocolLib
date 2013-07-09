package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketSteerVehicle extends Packet {

	public float sideways;
	public float forward;
	public boolean jump;
	public boolean unmount;

	public PacketSteerVehicle() {
	}

	public PacketSteerVehicle(float sideways, float forward, boolean jump, boolean unmount) {
		this.sideways = sideways;
		this.forward = forward;
		this.jump = jump;
		this.unmount = unmount;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.sideways = in.readFloat();
		this.forward = in.readFloat();
		this.jump = in.readBoolean();
		this.unmount = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeFloat(this.sideways);
		out.writeFloat(this.forward);
		out.writeBoolean(this.jump);
		out.writeBoolean(this.unmount);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 27;
	}

}
