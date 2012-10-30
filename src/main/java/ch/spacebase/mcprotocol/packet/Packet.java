package ch.spacebase.mcprotocol.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public abstract class Packet {
	
	public Packet() {
	}
	
	public abstract void read(DataInputStream in) throws IOException;
	
	public abstract void write(DataOutputStream out) throws IOException;
	
	public abstract void handleClient(Client conn);
	
	public abstract void handleServer(ServerConnection conn);
	
	public abstract int getId();
	
}
