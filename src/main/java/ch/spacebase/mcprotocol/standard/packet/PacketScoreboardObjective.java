package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketScoreboardObjective extends Packet {

	public String name;
	public String value;
	public byte action;

	public PacketScoreboardObjective() {
	}

	public PacketScoreboardObjective(String name, String value, byte action) {
		this.name = name;
		this.value = value;
		this.action = action;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.name = in.readString();
		this.value = in.readString();
		this.action = in.readByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.name);
		out.writeString(this.value);
		out.writeByte(this.action);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 206;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
