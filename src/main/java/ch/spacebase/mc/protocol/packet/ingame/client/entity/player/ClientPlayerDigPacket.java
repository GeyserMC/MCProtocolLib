package ch.spacebase.mc.protocol.packet.ingame.client.entity.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ch.spacebase.mc.protocol.packet.ingame.client.entity.ClientAnimationPacket.Animation;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientPlayerDigPacket implements Packet {
	
	private Status status;
	private int x;
	private int y;
	private int z;
	private Face face;
	
	public ClientPlayerDigPacket() {
	}
	
	public ClientPlayerDigPacket(Status status, int x, int y, int z, Face face) {
		this.status = status;
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public Face getFace() {
		return this.face;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.status = Status.values()[in.readUnsignedByte()];
		this.x = in.readInt();
		this.y = in.readUnsignedByte();
		this.z = in.readInt();
		this.face = Face.decode(in.readUnsignedByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.status.ordinal());
		out.writeInt(this.x);
		out.writeByte(this.y);
		out.writeInt(this.z);
		out.writeByte(this.face.encode());
	}
	
	@Override
	public boolean isPriority() {
		return false;
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
		BOTTOM(0),
		TOP(1),
		EAST(2),
		WEST(3),
		NORTH(4),
		SOUTH(5),
		INVALID(255);
		
		private static final Map<Integer, Face> lookup = 
			new HashMap<Integer, Face>();

		static {
			for (final Face a : Face.values()) {
				lookup.put(a.encode(), a);
			}
		}
		
		private final int faceId;

		private Face(final int faceId) {
			this.faceId = faceId;
		}
		
		public static Face decode(final int faceId) {
			return lookup.get(faceId);
		}

		private Integer encode() {
			return faceId;
		}
	}

}
