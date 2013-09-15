package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketUpdateSign extends Packet {

	public int x;
	public short y;
	public int z;
	public String lines[];

	public PacketUpdateSign() {
	}

	public PacketUpdateSign(int x, short y, int z, String lines[]) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.lines = lines;
		if(this.lines == null || this.lines.length != 4) throw new IllegalArgumentException("Line array size must be 4!");
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.x = in.readInt();
		this.y = in.readShort();
		this.z = in.readInt();
		this.lines = new String[4];
		this.lines[0] = in.readString();
		this.lines[1] = in.readString();
		this.lines[2] = in.readString();
		this.lines[3] = in.readString();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.x);
		out.writeShort(this.y);
		out.writeInt(this.z);
		out.writeString(this.lines[0]);
		out.writeString(this.lines[1]);
		out.writeString(this.lines[2]);
		out.writeString(this.lines[3]);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 130;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
