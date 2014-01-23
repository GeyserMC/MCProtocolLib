package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.Position;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

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

	@Override
	public void read(NetInput in) throws IOException {
		this.position = NetUtil.readPosition(in);
	}

	@Override
	public void write(NetOutput out) throws IOException {
		NetUtil.writePosition(out, this.position);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
