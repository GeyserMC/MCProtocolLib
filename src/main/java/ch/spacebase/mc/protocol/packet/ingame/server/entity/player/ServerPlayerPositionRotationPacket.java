package ch.spacebase.mc.protocol.packet.ingame.server.entity.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerPlayerPositionRotationPacket implements Packet {
	
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private List<Element> relative;
	
	@SuppressWarnings("unused")
	private ServerPlayerPositionRotationPacket() {
	}
	
	public ServerPlayerPositionRotationPacket(double x, double y, double z, float yaw, float pitch) {
		this(x, y, z, yaw, pitch, new Element[0]);
	}
	
	public ServerPlayerPositionRotationPacket(double x, double y, double z, float yaw, float pitch, Element... relative) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.relative = Arrays.asList(relative);
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public float getYaw() {
		return this.yaw;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	public List<Element> getRelativeElements() {
		return this.relative;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.x = in.readDouble();
		this.y = in.readDouble();
		this.z = in.readDouble();
		this.yaw = in.readFloat();
		this.pitch = in.readFloat();
		this.relative = new ArrayList<Element>();
		int flags = in.readUnsignedByte();
		for(Element element : Element.values()) {
			int bit = 1 << element.ordinal();
			if((flags & bit) == bit) {
				this.relative.add(element);
			}
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeDouble(this.x);
		out.writeDouble(this.y);
		out.writeDouble(this.z);
		out.writeFloat(this.yaw);
		out.writeFloat(this.pitch);
		int flags = 0;
		for(Element element : this.relative) {
			flags |= 1 << element.ordinal();
		}
		
		out.writeByte(flags);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Element {
		X,
		Y,
		Z,
		PITCH,
		YAW;
	}

}
