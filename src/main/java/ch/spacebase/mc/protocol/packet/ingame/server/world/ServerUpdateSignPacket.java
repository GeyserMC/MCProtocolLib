package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.Position;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerUpdateSignPacket implements Packet {
	
	private Position position;
	private String lines[];
	
	@SuppressWarnings("unused")
	private ServerUpdateSignPacket() {
	}
	
	public ServerUpdateSignPacket(Position position, String lines[]) {
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

	@Override
	public void read(NetInput in) throws IOException {
		this.position = NetUtil.readPosition(in);
		this.lines = new String[4];
		for(int count = 0; count < this.lines.length; count++) {
			this.lines[count] = in.readString();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		NetUtil.writePosition(out, this.position);
		for(String line : this.lines) {
			out.writeString(line);
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
