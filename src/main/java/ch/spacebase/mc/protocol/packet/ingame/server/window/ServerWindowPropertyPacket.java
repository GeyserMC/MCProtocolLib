package ch.spacebase.mc.protocol.packet.ingame.server.window;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.WindowProperty;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerWindowPropertyPacket implements Packet {
	
	private int windowId;
	private WindowProperty property;
	private int value;
	
	@SuppressWarnings("unused")
	private ServerWindowPropertyPacket() {
	}
	
	public ServerWindowPropertyPacket(int windowId, WindowProperty property, int value) {
		this.windowId = windowId;
		this.property = property;
		this.value = value;
	}
	
	public int getWindowId() {
		return this.windowId;
	}
	
	public WindowProperty getProperty() {
		return this.property;
	}
	
	public int getValue() {
		return this.value;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.windowId = in.readUnsignedByte();
		this.property = MagicValues.key(WindowProperty.class, in.readShort());
		this.value = in.readShort();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.windowId);
		out.writeShort(MagicValues.value(Integer.class, this.property));
		out.writeShort(this.value);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
