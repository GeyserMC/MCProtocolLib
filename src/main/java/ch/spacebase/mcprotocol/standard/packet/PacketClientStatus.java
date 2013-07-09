package ch.spacebase.mcprotocol.standard.packet;

import java.io.BufferedReader;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.StandardServer;
import ch.spacebase.mcprotocol.standard.StandardServerConnection;
import ch.spacebase.mcprotocol.util.Util;

public class PacketClientStatus extends Packet {

	public byte status;

	public PacketClientStatus() {
	}

	public PacketClientStatus(byte status) {
		this.status = status;
	}

	public byte getStatus() {
		return this.status;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.status = in.readByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.status);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
		if(this.status == 0 && conn.getServer().isAuthEnabled()) {
			String encrypted = new BigInteger(Util.encrypt(((StandardServerConnection) conn).getLoginKey(), ((StandardServer) conn.getServer()).getKeys().getPublic(), ((StandardServerConnection) conn).getSecretKey())).toString(16);
			String response = null;

			try {
				URL url = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(conn.getUsername(), "UTF-8") + "&serverId=" + URLEncoder.encode(encrypted, "UTF-8"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				response = reader.readLine();
				reader.close();
			} catch (IOException e) {
				response = e.toString();
			}

			if(!response.equals("YES")) {
				conn.disconnect("Failed to verify username!");
				return;
			}
		}

		for(ServerConnection c : conn.getServer().getConnections()) {
			if(c.getUsername().equals(conn.getUsername())) {
				c.disconnect("You logged in from another location!");
				break;
			}
		}
	}

	@Override
	public int getId() {
		return 205;
	}

}
