package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketDisplayScoreboard extends Packet {

	public byte position;
	public String scoreboard;

	public PacketDisplayScoreboard() {
	}

	public PacketDisplayScoreboard(byte position, String scoreboard) {
		this.position = position;
		this.scoreboard = scoreboard;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.position = in.readByte();
		this.scoreboard = in.readString();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.position);
		out.writeString(this.scoreboard);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 208;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
