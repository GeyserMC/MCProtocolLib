package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.data.StandardItemStack;
import ch.spacebase.mcprotocol.standard.io.StandardInput;
import ch.spacebase.mcprotocol.standard.io.StandardOutput;

public class PacketCreativeSlot extends Packet {

	public short slot;
	public StandardItemStack clicked;

	public PacketCreativeSlot() {
	}

	public PacketCreativeSlot(short slot, StandardItemStack clicked) {
		this.slot = slot;
		this.clicked = clicked;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.slot = in.readShort();
		this.clicked = ((StandardInput) in).readItem();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeShort(this.slot);
		if(this.clicked != null) {
			((StandardOutput) out).writeItem(this.clicked);
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
		return 107;
	}

}
