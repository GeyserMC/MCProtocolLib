package org.spacehq.mc.protocol.packet.ingame.client.world;

import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ClientUpdateSignPacket implements Packet {

	private Position position;
	private String lines[];

	@SuppressWarnings("unused")
	private ClientUpdateSignPacket() {
	}

	public ClientUpdateSignPacket(Position position, String lines[]) {
		if(lines.length != 4) {
			throw new IllegalArgumentException("Lines must contain exactly 4 strings!");
		}

		this.position = position;
		this.lines = lines;
	}

	public Position getPosition() {
		return this.position;
	}

	public String[] getLines() {
		return this.lines;
	}

	public void read(NetInput in) throws IOException {
		this.position = NetUtil.readPosition(in);
		this.lines = new String[4];
		for(int count = 0; count < this.lines.length; count++) {
			this.lines[count] = in.readString();
		}
	}

	public void write(NetOutput out) throws IOException {
		NetUtil.writePosition(out, this.position);
		for(String line : this.lines) {
			out.writeString(line);
		}
	}

	public boolean isPriority() {
		return false;
	}

}
