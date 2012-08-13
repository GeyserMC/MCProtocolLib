package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketIncrementStatistic extends Packet {

	public int statistic;
	public byte amount;
	
	public PacketIncrementStatistic() {
	}
	
	public PacketIncrementStatistic(int statistic, byte amount) {
		this.statistic = statistic;
		this.amount = amount;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.statistic = in.readInt();
		this.amount = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.statistic);
		out.writeByte(this.amount);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 200;
	}
	
}
