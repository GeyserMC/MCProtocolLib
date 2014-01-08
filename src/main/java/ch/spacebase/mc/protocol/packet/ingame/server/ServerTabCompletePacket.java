package ch.spacebase.mc.protocol.packet.ingame.server;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerTabCompletePacket implements Packet {
	
	private String matches[];
	
	@SuppressWarnings("unused")
	private ServerTabCompletePacket() {
	}
	
	public ServerTabCompletePacket(String matches[]) {
		this.matches = matches;
	}
	
	public String[] getMatches() {
		return this.matches;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.matches = new String[in.readVarInt()];
		for(int index = 0; index < this.matches.length; index++) {
			this.matches[index] = in.readString();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.matches.length);
		for(String match : this.matches) {
			out.writeString(match);
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
