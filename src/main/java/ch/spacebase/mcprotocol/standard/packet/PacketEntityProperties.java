package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.data.EntityProperty;
import ch.spacebase.mcprotocol.standard.data.Unknown;

public class PacketEntityProperties extends Packet {

	public int entityId;
	public List<EntityProperty> properties;

	public PacketEntityProperties() {
	}

	public PacketEntityProperties(int entityId, List<EntityProperty> properties) {
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
		this.properties = new ArrayList<EntityProperty>();
		for(int ct = 0; ct < count; ct++) {
			EntityProperty prop = new EntityProperty(in.readString(), in.readDouble());
			short len = in.readShort();
			for(int i = 0; i < len; i++) {
				prop.addUnknown(new Unknown(new UUID(in.readLong(), in.readLong()), in.readDouble(), in.readByte()));
			}
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeInt(this.properties.size());
		for(EntityProperty prop : this.properties) {
			out.writeString(prop.getName());
			out.writeDouble(prop.getValue());
			out.writeShort(prop.getUnknowns().size());
			for(Unknown u : prop.getUnknowns()) {
				out.writeLong(u.getUID().getMostSignificantBits());
				out.writeLong(u.getUID().getLeastSignificantBits());
				out.writeDouble(u.getAmount());
				out.writeByte(u.getOperation());
			}
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
