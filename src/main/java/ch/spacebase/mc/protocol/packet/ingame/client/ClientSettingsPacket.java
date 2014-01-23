package ch.spacebase.mc.protocol.packet.ingame.client;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientSettingsPacket implements Packet {
	
	private String locale;
	private int renderDistance;
	private ChatVisibility chatVisibility;
	private boolean chatColors;
	private int skinFlags;
	
	@SuppressWarnings("unused")
	private ClientSettingsPacket() {
	}
	
	public ClientSettingsPacket(String locale, int renderDistance, ChatVisibility chatVisibility, boolean chatColors, int skinFlags) {
		this.locale = locale;
		this.renderDistance = renderDistance;
		this.chatVisibility = chatVisibility;
		this.chatColors = chatColors;
		this.skinFlags = skinFlags;
	}
	
	public String getLocale() {
		return this.locale;
	}
	
	public int getRenderDistance() {
		return this.renderDistance;
	}
	
	public ChatVisibility getChatVisibility() {
		return this.chatVisibility;
	}
	
	public boolean getUseChatColors() {
		return this.chatColors;
	}
	
	public int getSkinFlags() {
		return this.skinFlags;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.locale = in.readString();
		this.renderDistance = in.readByte();
		this.chatVisibility = ChatVisibility.values()[in.readByte()];
		this.chatColors = in.readBoolean();
		this.skinFlags = in.readUnsignedByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.locale);
		out.writeByte(this.renderDistance);
		out.writeByte(this.chatVisibility.ordinal());
		out.writeBoolean(this.chatColors);
		out.writeByte(this.skinFlags);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum ChatVisibility {
		FULL,
		SYSTEM,
		HIDDEN;
	}
	
	public static enum Difficulty {
		PEACEFUL,
		EASY,
		NORMAL,
		HARD;
	}

}
