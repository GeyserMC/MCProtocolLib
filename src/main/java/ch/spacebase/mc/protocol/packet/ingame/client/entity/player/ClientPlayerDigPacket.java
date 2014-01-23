package ch.spacebase.mc.protocol.packet.ingame.client.entity.player;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.Position;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientPlayerDigPacket implements Packet {
	
	private Status status;
	private Position position;
	private Face face;
	
	@SuppressWarnings("unused")
	private ClientPlayerDigPacket() {
	}
	
	public ClientPlayerDigPacket(Status status, Position position, Face face) {
		this.status = status;
		this.position = position;
		this.face = face;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public Face getFace() {
		return this.face;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.status = Status.values()[in.readUnsignedByte()];
		this.position = NetUtil.readPosition(in);
		this.face = valueToFace(in.readUnsignedByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.status.ordinal());
		NetUtil.writePosition(out, this.position);
		out.writeByte(faceToValue(this.face));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	private static Face valueToFace(int value) {
		switch(value) {
			case 0:
				return Face.BOTTOM;
			case 1:
				return Face.TOP;
			case 2:
				return Face.EAST;
			case 3:
				return Face.WEST;
			case 4:
				return Face.NORTH;
			case 5:
				return Face.SOUTH;
			default:
				return Face.INVALID;
		}
	}
	
	private static int faceToValue(Face face) {
		switch(face) {
			case BOTTOM:
				return 0;
			case TOP:
				return 1;
			case EAST:
				return 2;
			case WEST:
				return 3;
			case NORTH:
				return 4;
			case SOUTH:
				return 5;
			default:
				return 255;
		}
	}
	
	public static enum Status {
		START_DIGGING,
		CANCEL_DIGGING,
		FINISH_DIGGING,
		DROP_ITEM_STACK,
		DROP_ITEM,
		SHOOT_ARROW_OR_FINISH_EATING;
	}
	
	public static enum Face {
		BOTTOM,
		TOP,
		EAST,
		WEST,
		NORTH,
		SOUTH,
		INVALID;
	}

}
