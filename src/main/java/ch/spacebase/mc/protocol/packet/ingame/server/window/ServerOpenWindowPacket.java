package ch.spacebase.mc.protocol.packet.ingame.server.window;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerOpenWindowPacket implements Packet {
	
	private int windowId;
	private String type;
	private String name;
	private int slots;
	private boolean useName;
	private int ownerEntityId;
	
	@SuppressWarnings("unused")
	private ServerOpenWindowPacket() {
	}
	
	public ServerOpenWindowPacket(int windowId, String type, String name, int slots, boolean useName) {
		this(windowId, type, name, slots, useName, 0);
	}
	
	public ServerOpenWindowPacket(int windowId, String type, String name, int slots, boolean useName, int ownerEntityId) {
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
	
	public String getType() {
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
		this.type = in.readString();
		this.name = in.readString();
		this.slots = in.readUnsignedByte();
		this.useName = in.readBoolean();
		if(this.type.equals(Type.HORSE)) {
			this.ownerEntityId = in.readInt();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.windowId);
		out.writeString(this.type);
		out.writeString(this.name);
		out.writeByte(this.slots);
		out.writeBoolean(this.useName);
		if(this.type.equals(Type.HORSE)) {
			out.writeInt(this.ownerEntityId);
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static class Type {
		public static final String GENERIC_INVENTORY = "minecraft:container";
		public static final String ANVIL = "minecraft:anvil";
		public static final String BEACON = "minecraft:beacon";
		public static final String BREWING_STAND = "minecraft:brewing_stand";
		public static final String CHEST = "minecraft:chest";
		public static final String CRAFTING_TABLE = "minecraft:crafting_table";
		public static final String DISPENSER = "minecraft:dispenser";
		public static final String DROPPER = "minecraft:dropper";
		public static final String ENCHANTING_TABLE = "minecraft:enchanting_table";
		public static final String FURNACE = "minecraft:furnace";
		public static final String HOPPER = "minecraft:hopper";
		public static final String VILLAGER = "minecraft:villager";
		public static final String HORSE = "EntityHorse";
	}

}
