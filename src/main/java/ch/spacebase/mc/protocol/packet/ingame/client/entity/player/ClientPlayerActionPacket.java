package ch.spacebase.mc.protocol.packet.ingame.client.entity.player;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.Position;
import ch.spacebase.mc.protocol.data.game.values.Face;
import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.PlayerAction;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientPlayerActionPacket implements Packet {
	
	private PlayerAction action;
	private Position position;
	private Face face;
	
	@SuppressWarnings("unused")
	private ClientPlayerActionPacket() {
	}
	
	public ClientPlayerActionPacket(PlayerAction action, Position position, Face face) {
		this.action = action;
		this.position = position;
		this.face = face;
	}
	
	public PlayerAction getAction() {
		return this.action;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public Face getFace() {
		return this.face;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.action = MagicValues.key(PlayerAction.class, in.readUnsignedByte());
		this.position = NetUtil.readPosition(in);
		this.face = MagicValues.key(Face.class, in.readUnsignedByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(MagicValues.value(Integer.class, this.action));
		NetUtil.writePosition(out, this.position);
		out.writeByte(MagicValues.value(Integer.class, this.face));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
