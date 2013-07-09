package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketUpdateTileEntity extends Packet {

	public int x;
	public short y;
	public int z;
	public byte action;
	public byte nbt[];

	public PacketUpdateTileEntity() {
	}

	public PacketUpdateTileEntity(int x, short y, int z, byte action, byte nbt[]) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.action = action;
		this.nbt = nbt;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.x = in.readInt();
		this.y = in.readShort();
		this.z = in.readInt();
		this.action = in.readByte();
		this.nbt = in.readBytes(in.readShort());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.x);
		out.writeShort(this.y);
		out.writeInt(this.z);
		out.writeByte(this.action);
		if(this.nbt != null) {
			out.writeShort(this.nbt.length);
			out.writeBytes(this.nbt);
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
		return 132;
	}

}
