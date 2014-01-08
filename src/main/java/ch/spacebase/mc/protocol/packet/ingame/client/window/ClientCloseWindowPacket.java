package ch.spacebase.mc.protocol.packet.ingame.client.window;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientCloseWindowPacket implements Packet {
	
	private int windowId;
	
	@SuppressWarnings("unused")
	private ClientCloseWindowPacket() {
	}
	
	public ClientCloseWindowPacket(int windowId) {
		this.windowId = windowId;
	}
	
	public int getWindowId() {
		return this.windowId;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.windowId = in.readByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.windowId);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
