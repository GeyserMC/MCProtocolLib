package ch.spacebase.mc.protocol.packet.ingame.client.entity.player;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.ItemStack;
import ch.spacebase.mc.protocol.data.game.Position;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientPlayerPlaceBlockPacket implements Packet {
	
	private Position position;
	private Face face;
	private ItemStack held;
	private float cursorX;
	private float cursorY;
	private float cursorZ;
	
	@SuppressWarnings("unused")
	private ClientPlayerPlaceBlockPacket() {
	}
	
	public ClientPlayerPlaceBlockPacket(Position position, Face face, ItemStack held, float cursorX, float cursorY, float cursorZ) {
		this.position = position;
		this.face = face;
		this.held = held;
		this.cursorX = cursorX;
		this.cursorY = cursorY;
		this.cursorZ = cursorZ;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public Face getFace() {
		return this.face;
	}
	
	public ItemStack getHeldItem() {
		return this.held;
	}
	
	public float getCursorX() {
		return this.cursorX;
	}
	
	public float getCursorY() {
		return this.cursorY;
	}
	
	public float getCursorZ() {
		return this.cursorZ;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.position = NetUtil.readPosition(in);
		this.face = Face.values()[in.readUnsignedByte()];
		this.held = NetUtil.readItem(in);
		this.cursorX = in.readByte() / 16f;
		this.cursorY = in.readByte() / 16f;
		this.cursorZ = in.readByte() / 16f;
	}

	@Override
	public void write(NetOutput out) throws IOException {
		NetUtil.writePosition(out, this.position);
		out.writeByte(this.face.ordinal());
		NetUtil.writeItem(out, this.held);
		out.writeByte((int) (this.cursorX * 16));
		out.writeByte((int) (this.cursorY * 16));
		out.writeByte((int) (this.cursorZ * 16));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Face {
		BOTTOM,
		TOP,
		EAST,
		WEST,
		NORTH,
		SOUTH;
	}

}
