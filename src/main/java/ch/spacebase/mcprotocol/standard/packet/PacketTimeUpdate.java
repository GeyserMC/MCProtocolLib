package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketTimeUpdate extends Packet {

	public long age;
	public long time;

	public PacketTimeUpdate() {
	}

	public PacketTimeUpdate(long age, long time) {
		this.age = age;
		this.time = time;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.age = in.readLong();
		this.time = in.readLong();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeLong(this.age);
		out.writeLong(this.time);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 4;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
