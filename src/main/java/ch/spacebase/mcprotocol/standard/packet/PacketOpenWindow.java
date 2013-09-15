package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.Constants;

public class PacketOpenWindow extends Packet {

	public byte id;
	public byte type;
	public String name;
	public byte slots;
	public boolean useTitle;
	public int horseId;

	public PacketOpenWindow() {
	}
	
	public PacketOpenWindow(byte id, byte type, String name, byte slots, boolean useTitle) {
		this(id, type, name, slots, useTitle, 0);
	}

	public PacketOpenWindow(byte id, byte type, String name, byte slots, boolean useTitle, int horseId) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.slots = slots;
		this.useTitle = useTitle;
		this.horseId = horseId;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.id = in.readByte();
		this.type = in.readByte();
		this.name = in.readString();
		this.slots = in.readByte();
		this.useTitle = in.readBoolean();
		if(this.type == Constants.StandardProtocol.WindowTypeIds.HORSE) {
			this.horseId = in.readInt();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.id);
		out.writeByte(this.type);
		out.writeString(this.name);
		out.writeByte(this.slots);
		out.writeBoolean(this.useTitle);
		if(this.type == Constants.StandardProtocol.WindowTypeIds.HORSE) {
			out.writeInt(this.horseId);
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
		return 100;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
