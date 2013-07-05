package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PublicKey;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.StandardProtocol;
import ch.spacebase.mcprotocol.util.Constants;
import ch.spacebase.mcprotocol.util.IOUtils;
import ch.spacebase.mcprotocol.util.Util;

public class PacketHandshake extends Packet {

	public int protocol;
	public String user;
	public String host;
	public int port;

	public PacketHandshake() {
	}

	public PacketHandshake(String user, String host, int port) {
		this.protocol = Constants.StandardProtocol.PROTOCOL_VERSION;
		this.user = user;
		this.host = host;
		this.port = port;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.protocol = in.readByte();
		this.user = IOUtils.readString(in);
		this.host = IOUtils.readString(in);
		this.port = in.readInt();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(this.protocol);
		IOUtils.writeString(out, this.user);
		IOUtils.writeString(out, this.host);
		out.writeInt(this.port);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
		if(!Util.stripColor(this.user).equals(this.user)) {
			conn.disconnect("Invalid username.");
		} else if(this.protocol != Constants.StandardProtocol.PROTOCOL_VERSION) {
			if(this.protocol > Constants.StandardProtocol.PROTOCOL_VERSION) {
				conn.disconnect("Outdated Server!");
			} else {
				conn.disconnect("Outdated Client!");
			}
		} else {
			conn.setUsername(this.user);
			PublicKey key = conn.getServer().getKeys().getPublic();
			((StandardProtocol) conn.getProtocol()).setLoginKey(conn.getServer().verifyUsers() ? Long.toString(Util.random().nextLong(), 16) : "-");
			byte token[] = new byte[4];
			Util.random().nextBytes(token);
			((StandardProtocol) conn.getProtocol()).setToken(token);

			conn.send(new PacketKeyRequest(((StandardProtocol) conn.getProtocol()).getLoginKey(), key.getEncoded(), token));
		}
	}

	@Override
	public int getId() {
		return 2;
	}

}
