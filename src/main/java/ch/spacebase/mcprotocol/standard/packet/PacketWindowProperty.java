package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketWindowProperty extends Packet {

	public byte id;
	public short prop;
	public short value;

	public PacketWindowProperty() {
	}

	public PacketWindowProperty(byte id, short prop, short value) {
		this.id = id;
		this.prop = prop;
		this.value = value;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.id = in.readByte();
		this.prop = in.readShort();
		this.value = in.readShort();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.id);
		out.writeShort(this.prop);
		out.writeShort(this.value);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 105;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
