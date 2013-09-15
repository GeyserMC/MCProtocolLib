package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketEnchantItem extends Packet {

	public byte id;
	public byte enchantment;

	public PacketEnchantItem() {
	}

	public PacketEnchantItem(byte id, byte enchantment) {
		this.id = id;
		this.enchantment = enchantment;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.id = in.readByte();
		this.enchantment = in.readByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.id);
		out.writeByte(this.enchantment);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 108;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
