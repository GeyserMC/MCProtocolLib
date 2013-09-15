package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketItemData extends Packet {

	public short item;
	public short damage;
	public byte data[];

	public PacketItemData() {
	}

	public PacketItemData(short item, short damage, byte data[]) {
		this.item = item;
		this.damage = damage;
		this.data = data;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.item = in.readShort();
		this.damage = in.readShort();
		this.data = in.readBytes(in.readShort());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeShort(this.item);
		out.writeShort(this.damage);
		out.writeShort(this.data.length);
		out.writeBytes(this.data);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 131;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
