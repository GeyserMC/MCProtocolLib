package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;
import java.security.PublicKey;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.StandardServer;
import ch.spacebase.mcprotocol.standard.StandardServerConnection;
import ch.spacebase.mcprotocol.util.Constants;
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
	public void read(NetInput in) throws IOException {
		this.protocol = in.readByte();
		this.user = in.readString();
		this.host = in.readString();
		this.port = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.protocol);
		out.writeString(this.user);
		out.writeString(this.host);
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
			PublicKey key = ((StandardServer) conn.getServer()).getKeys().getPublic();
			((StandardServerConnection) conn).setLoginKey(conn.getServer().isAuthEnabled() ? Long.toString(Util.random().nextLong(), 16) : "-");
			byte token[] = new byte[4];
			Util.random().nextBytes(token);
			((StandardServerConnection) conn).setToken(token);

			conn.send(new PacketKeyRequest(((StandardServerConnection) conn).getLoginKey(), key.getEncoded(), token));
		}
	}

	@Override
	public int getId() {
		return 2;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
