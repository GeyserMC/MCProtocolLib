package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketBlockAction extends Packet {

	public int x;
	public int y;
	public int z;
	public byte b1;
	public byte b2;
	public int block;

	public PacketBlockAction() {
	}

	public PacketBlockAction(int x, int y, int z, byte b1, byte b2, short block) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.b1 = b1;
		this.b2 = b2;
		this.block = block;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.x = in.readInt();
		this.y = in.readShort();
		this.z = in.readInt();
		this.b1 = in.readByte();
		this.b2 = in.readByte();
		this.block = in.readUnsignedShort();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.x);
		out.writeShort(this.y);
		out.writeInt(this.z);
		out.writeByte(this.b1);
		out.writeByte(this.b2);
		out.writeShort(this.block);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 54;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
