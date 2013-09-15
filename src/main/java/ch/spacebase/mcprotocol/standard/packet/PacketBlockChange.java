package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketBlockChange extends Packet {

	public int x;
	public int y;
	public int z;
	public short block;
	public byte data;

	public PacketBlockChange() {
	}

	public PacketBlockChange(int x, int y, int z, short block, byte data) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.block = block;
		this.data = data;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.x = in.readInt();
		this.y = in.readByte();
		this.z = in.readInt();
		this.block = in.readShort();
		this.data = in.readByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.x);
		out.writeByte(this.y);
		out.writeInt(this.z);
		out.writeShort(this.block);
		out.writeByte(this.data);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 53;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
