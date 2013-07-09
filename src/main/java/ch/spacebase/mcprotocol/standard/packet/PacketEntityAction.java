package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketEntityAction extends Packet {

	public int entityId;
	public byte action;
	public int jumpBar;

	public PacketEntityAction() {
	}

	public PacketEntityAction(int entityId, byte action) {
		this(entityId, action, 0);
	}
	
	public PacketEntityAction(int entityId, byte action, int jumpBar) {
		this.entityId = entityId;
		this.action = action;
		this.jumpBar = jumpBar;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.action = in.readByte();
		this.jumpBar = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.action);
		out.writeInt(this.jumpBar);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 19;
	}

}
