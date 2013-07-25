package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketEffect extends Packet {

	public int effectId;
	public int x;
	public int y;
	public int z;
	public int data;
	public boolean ignoreVolume;

	public PacketEffect() {
	}

	public PacketEffect(int effectId, int x, int y, int z, int data, boolean ignoreVolume) {
		this.effectId = effectId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
		this.ignoreVolume = ignoreVolume;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.effectId = in.readInt();
		this.x = in.readInt();
		this.y = in.readUnsignedByte();
		this.z = in.readInt();
		this.data = in.readInt();
		this.ignoreVolume = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.effectId);
		out.writeInt(this.x);
		out.writeByte(this.y);
		out.writeInt(this.z);
		out.writeInt(this.data);
		out.writeBoolean(this.ignoreVolume);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 61;
	}

}
