package org.spacehq.mc.protocol.packet.ingame.client;

import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ClientTabCompletePacket implements Packet {

	private String text;
	private Position position;

	@SuppressWarnings("unused")
	private ClientTabCompletePacket() {
	}

	public ClientTabCompletePacket(String text) {
		this(text, null);
	}

	public ClientTabCompletePacket(String text, Position position) {
		this.text = text;
		this.position = position;
	}

	public String getText() {
		return this.text;
	}

	public void read(NetInput in) throws IOException {
		this.text = in.readString();
		this.position = in.readBoolean() ? NetUtil.readPosition(in) : null;
	}

	public void write(NetOutput out) throws IOException {
		out.writeString(this.text);
		out.writeBoolean(this.position != null);
		if(this.position != null) {
			NetUtil.writePosition(out, this.position);
		}
	}

	public boolean isPriority() {
		return false;
	}

}
