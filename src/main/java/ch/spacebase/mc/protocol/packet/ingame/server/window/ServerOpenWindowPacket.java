package ch.spacebase.mc.protocol.packet.ingame.server.window;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerOpenWindowPacket implements Packet {
	
	private int windowId;
	private Type type;
	private String name;
	private int slots;
	private boolean useName;
	private int ownerEntityId;
	
	@SuppressWarnings("unused")
	private ServerOpenWindowPacket() {
	}
	
	public ServerOpenWindowPacket(int windowId, Type type, String name, int slots, boolean useName) {
		this(windowId, type, name, slots, useName, 0);
	}
	
	public ServerOpenWindowPacket(int windowId, Type type, String name, int slots, boolean useName, int ownerEntityId) {
		this.windowId = windowId;
		this.type = type;
		this.name = name;
		this.slots = slots;
		this.useName = useName;
		this.ownerEntityId = ownerEntityId;
	}
	
	public int getWindowId() {
		return this.windowId;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSlots() {
		return this.slots;
	}
	
	public boolean getUseName() {
		return this.useName;
	}
	
	public int getOwnerEntityId() {
		return this.ownerEntityId;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.windowId = in.readUnsignedByte();
		this.type = Type.values()[in.readUnsignedByte()];
		this.name = in.readString();
		this.slots = in.readUnsignedByte();
		this.useName = in.readBoolean();
		if(this.type == Type.HORSE_INVENTORY) {
			this.ownerEntityId = in.readInt();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.windowId);
		out.writeByte(this.type.ordinal());
		out.writeString(this.name);
		out.writeByte(this.slots);
		out.writeBoolean(this.useName);
		if(this.type == Type.HORSE_INVENTORY) {
			out.writeInt(this.ownerEntityId);
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Type {
		CHEST,
		CRAFTING_TABLE,
		FURNACE,
		DISPENSER,
		ENCHANTMENT_TABLE,
		BREWING_STAND,
		VILLAGER_TRADE,
		BEACON,
		ANVIL,
		HOPPER,
		DROPPER,
		HORSE_INVENTORY;
	}

}
