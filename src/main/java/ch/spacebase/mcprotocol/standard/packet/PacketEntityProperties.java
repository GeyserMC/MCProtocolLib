package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

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
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		int count = in.readInt();
		this.properties = new HashMap<String, Double>();
		for(int ct = 0; ct < count; ct++) {
			this.properties.put(in.readString(), in.readDouble());
			// TODO: implement properly when further docs are available
			short len = in.readShort();
			for(int i = 0; i < len; i++) {
				UUID uuid = new UUID(in.readLong(), in.readLong());
				double d = in.readDouble();
				byte b = in.readByte();
			}
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeInt(this.properties.size());
		for(String key : this.properties.keySet()) {
			out.writeString(key);
			out.writeDouble(this.properties.get(key));
			// TODO: implement properly when further docs are available
			out.writeShort(0);
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
