package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerSpawnPositionPacket implements Packet {

	private Position position;

	@SuppressWarnings("unused")
	private ServerSpawnPositionPacket() {
	}

	public ServerSpawnPositionPacket(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return this.position;
	}

	public void read(NetInput in) throws IOException {
		this.position = NetUtil.readPosition(in);
	}

	public void write(NetOutput out) throws IOException {
		NetUtil.writePosition(out, this.position);
	}

	public boolean isPriority() {
		return false;
	}

}
