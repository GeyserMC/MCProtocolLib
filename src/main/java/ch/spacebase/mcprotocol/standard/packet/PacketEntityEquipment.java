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

public class PacketEntityEquipment extends Packet {

	public int entityId;
	public short slot;
	public StandardItemStack item;

	public PacketEntityEquipment() {
	}

	public PacketEntityEquipment(int entityId, short slot, StandardItemStack item) {
		this.entityId = entityId;
		this.slot = slot;
		this.item = item;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public short getSlot() {
		return this.slot;
	}

	public StandardItemStack getItem() {
		return this.item;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.slot = in.readShort();
		this.item = ((StandardInput) in).readItem();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeShort(this.slot);
		((StandardOutput) out).writeItem(this.item);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 5;
	}

}
