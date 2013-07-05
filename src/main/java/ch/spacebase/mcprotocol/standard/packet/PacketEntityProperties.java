package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketEntityProperties extends Packet {

	public int entityId;
	public Map<String, Double> properties;

	public PacketEntityProperties() {
	}

	public PacketEntityProperties(int entityId, Map<String, Double> properties) {
		if(properties.size() == 0) {
			throw new IllegalArgumentException("Properties map is empty.");
		}
		
		this.entityId = entityId;
		this.properties = properties;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		int count = in.readInt();
		this.properties = new HashMap<String, Double>();
		for(int ct = 0; ct < count; ct++) {
			this.properties.put(IOUtils.readString(in), in.readDouble());
		}
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		out.writeInt(this.properties.size());
		for(String key : this.properties.keySet()) {
			IOUtils.writeString(out, key);
			out.writeDouble(this.properties.get(key));
		}
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 44;
	}

}
