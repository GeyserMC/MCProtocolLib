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
	private Difficulty difficulty;
	private boolean capes;
	
	@SuppressWarnings("unused")
	private ClientSettingsPacket() {
	}
	
	public ClientSettingsPacket(String locale, int renderDistance, ChatVisibility chatVisibility, boolean chatColors, Difficulty difficulty, boolean capes) {
		this.locale = locale;
		this.renderDistance = renderDistance;
		this.chatVisibility = chatVisibility;
		this.chatColors = chatColors;
		this.difficulty = difficulty;
		this.capes = capes;
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
	
	public Difficulty getDifficulty() {
		return this.difficulty;
	}
	
	public boolean getShowCapes() {
		return this.capes;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.locale = in.readString();
		this.renderDistance = in.readByte();
		this.chatVisibility = ChatVisibility.values()[in.readByte()];
		this.chatColors = in.readBoolean();
		this.difficulty = Difficulty.values()[in.readByte()];
		this.capes = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.locale);
		out.writeByte(this.renderDistance);
		out.writeByte(this.chatVisibility.ordinal());
		out.writeBoolean(this.chatColors);
		out.writeByte(this.difficulty.ordinal());
		out.writeBoolean(this.capes);
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
