package ch.spacebase.mcprotocol.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

/**
 * A network data packet.
 */
public abstract class Packet {

	/**
	 * Creates an empty packet for reading to.
	 */
	public Packet() {
	}

	/**
	 * Reads the packet's data from the given input stream.
	 * @param in Input stream to read from.
	 * @throws IOException If an I/O error occurs.
	 */
	public abstract void read(DataInputStream in) throws IOException;

	/**
	 * Writes the packet's data to the given output stream.
	 * @param out Output stream to write to.
	 * @throws IOException If an I/O error occurs.
	 */
	public abstract void write(DataOutputStream out) throws IOException;

	/**
	 * Handles this packet when received by a client.
	 * @param conn Client receiving the packet.
	 */
	public abstract void handleClient(Client conn);

	/**
	 * Handles this packet when received by a server.
	 * @param conn Client connection sending the packet.
	 */
	public abstract void handleServer(ServerConnection conn);

	/**
	 * Gets the id of this packet.
	 * @return The packet's id.
	 */
	public abstract int getId();

}
