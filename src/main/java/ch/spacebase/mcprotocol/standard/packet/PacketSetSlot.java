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

public class PacketSetSlot extends Packet {

	public byte id;
	public short slot;
	public StandardItemStack item;

	public PacketSetSlot() {
	}

	public PacketSetSlot(byte id, short slot, StandardItemStack item) {
		this.id = id;
		this.slot = slot;
		this.item = item;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.id = in.readByte();
		this.slot = in.readShort();
		this.item = ((StandardInput) in).readItem();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.id);
		out.writeShort(this.slot);
		if(this.item != null) {
			((StandardOutput) out).writeItem(this.item);
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
		return 103;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
