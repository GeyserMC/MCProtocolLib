package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketConfirmTransaction extends Packet {

	public byte id;
	public short action;
	public boolean confirm;

	public PacketConfirmTransaction() {
	}

	public PacketConfirmTransaction(byte id, short action, boolean confirm) {
		this.id = id;
		this.action = action;
		this.confirm = confirm;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.id = in.readByte();
		this.action = in.readShort();
		this.confirm = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.id);
		out.writeShort(this.action);
		out.writeBoolean(this.confirm);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 106;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
