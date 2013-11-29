package ch.spacebase.packetlib.packet;

import ch.spacebase.packetlib.Client;
import ch.spacebase.packetlib.Server;
import ch.spacebase.packetlib.Session;

/**
 * A protocol for packet sending and receiving.
 */
public abstract class PacketProtocol {

	@SuppressWarnings("unchecked")
	private final Class<? extends Packet> registry[] = (Class<? extends Packet>[]) new Class<?>[256];
	
	/**
	 * Called when a client is created with this protocol.
	 * @param client The client that the session belongs to.
	 * @param session The created session.
	 */
	public abstract void newClientSession(Client client, Session session);
	
	/**
	 * Called when a server is created with this protocol.
	 * @param server The created server.
	 */
	public abstract void newServer(Server server);
	
	/**
	 * Called when a server's session is created with this protocol.
	 * @param server The server that the session belongs to.
	 * @param session The created session.
	 */
	public abstract void newServerSession(Server server, Session session);
	
	/**
	 * Decodes the given data array into readable form.
	 * @param data Data to decode.
	 * @return The decoded data.
	 */
	public byte[] decode(byte data[]) {
		return data;
	}
	
	/**
	 * Encodes the given data array for sending over the network.
	 * @param data Data to encode.
	 * @return The encoded data.
	 */
	public byte[] encode(byte data[]) {
		return data;
	}
	
	/**
	 * Registers a packet to this protocol.
	 * @param packet Packet to register.
	 */
	public final void register(Class<? extends Packet> packet) {
		int id = 0;
		try {
			id = packet.getDeclaredConstructor().newInstance().getId();
		} catch(NoSuchMethodError e) {
			System.err.println("Packet \"" + packet.getName() + "\" does not have a no-params constructor for instantiation.");
		} catch(Exception e) {
			System.err.println("Failed to instantiate packet " + packet.getName() + " to get id.");
			e.printStackTrace();
		}
		
		registry[id] = packet;
	}
	
	/**
	 * Creates a new instance of the packet with the given id.
	 * @param id Id of the packet to create, or null if it could not be created.
	 * @return The created packet
	 * @throws IllegalArgumentException If the packet ID is invalid.
	 */
	public final Packet createPacket(int id) {
		if(id < 0 || id >= registry.length || registry[id] == null) {
			throw new IllegalArgumentException("Invalid packet id: " + id);
		}
		
		try {
			return registry[id].getDeclaredConstructor().newInstance();
		} catch(NoSuchMethodError e) {
			System.err.println("Packet \"" + id + "\" does not have a no-params constructor for instantiation.");
		} catch(Exception e) {
			System.err.println("Failed to instantiate packet " + id + ".");
			e.printStackTrace();
		}
		
		return null;
	}
	
}
