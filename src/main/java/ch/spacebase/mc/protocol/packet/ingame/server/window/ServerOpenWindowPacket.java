package ch.spacebase.mc.protocol.packet.ingame.server.window;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.window.WindowType;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerOpenWindowPacket implements Packet {
	
	private int windowId;
	private WindowType type;
	private String name;
	private int slots;
	private boolean useName;
	private int ownerEntityId;
	
	@SuppressWarnings("unused")
	private ServerOpenWindowPacket() {
	}
	
	public ServerOpenWindowPacket(int windowId, WindowType type, String name, int slots, boolean useName) {
		this(windowId, type, name, slots, useName, 0);
	}
	
	public ServerOpenWindowPacket(int windowId, WindowType type, String name, int slots, boolean useName, int ownerEntityId) {
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
	
	public WindowType getType() {
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
		this.type = MagicValues.key(WindowType.class, in.readString());
		this.name = in.readString();
		this.slots = in.readUnsignedByte();
		this.useName = in.readBoolean();
		if(this.type == WindowType.HORSE) {
			this.ownerEntityId = in.readInt();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.windowId);
		out.writeString(MagicValues.value(String.class, this.type));
		out.writeString(this.name);
		out.writeByte(this.slots);
		out.writeBoolean(this.useName);
		if(this.type == WindowType.HORSE) {
			out.writeInt(this.ownerEntityId);
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
