package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.data.StandardItemStack;
import ch.spacebase.mcprotocol.standard.io.StandardInput;
import ch.spacebase.mcprotocol.standard.io.StandardOutput;

public class PacketWindowItems extends Packet {

	public byte id;
	public StandardItemStack items[];

	public PacketWindowItems() {
	}

	public PacketWindowItems(byte id, StandardItemStack items[]) {
		this.id = id;
		this.items = items;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.id = in.readByte();
		this.items = new StandardItemStack[in.readShort()];
		for(int count = 0; count < this.items.length; count++) {
			this.items[count] = ((StandardInput) in).readItem();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.id);
		out.writeShort(this.items.length);
		for(StandardItemStack item : this.items) {
			((StandardOutput) out).writeItem(item);
		}
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 104;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
