package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketUseEntity extends Packet {

	public int user;
	public int target;
	public boolean leftclick;

	public PacketUseEntity() {
	}

	public PacketUseEntity(int user, int target, boolean leftclick) {
		this.user = user;
		this.target = target;
		this.leftclick = leftclick;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.user = in.readInt();
		this.target = in.readInt();
		this.leftclick = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.user);
		out.writeInt(this.target);
		out.writeBoolean(this.leftclick);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 7;
	}

}
