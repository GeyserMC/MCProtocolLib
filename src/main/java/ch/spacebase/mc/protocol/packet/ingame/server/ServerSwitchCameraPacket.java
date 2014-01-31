package ch.spacebase.mc.protocol.packet.ingame.server;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerSwitchCameraPacket implements Packet {

	private int cameraEntityId;

	@SuppressWarnings("unused")
	private ServerSwitchCameraPacket() {
	}

	public ServerSwitchCameraPacket(int cameraEntityId) {
		this.cameraEntityId = cameraEntityId;
	}

	public int getCameraEntityId() {
		return this.cameraEntityId;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.cameraEntityId = in.readVarInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.cameraEntityId);
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
