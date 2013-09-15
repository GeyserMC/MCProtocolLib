package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.data.EntityAttribute;
import ch.spacebase.mcprotocol.standard.data.AttributeModifier;

public class PacketEntityAttributes extends Packet {

	public int entityId;
	public List<EntityAttribute> attributes;

	public PacketEntityAttributes() {
	}

	public PacketEntityAttributes(int entityId, List<EntityAttribute> attributes) {
		if(attributes.size() == 0) {
			throw new IllegalArgumentException("Attribute map is empty.");
		}
		this.entityId = entityId;
		this.attributes = attributes;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		int count = in.readInt();
		this.attributes = new ArrayList<EntityAttribute>();
		for(int ct = 0; ct < count; ct++) {
			EntityAttribute attrib = new EntityAttribute(in.readString(), in.readDouble());
			short len = in.readShort();
			for(int i = 0; i < len; i++) {
				attrib.addModifier(new AttributeModifier(new UUID(in.readLong(), in.readLong()), in.readDouble(), in.readByte()));
			}
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeInt(this.attributes.size());
		for(EntityAttribute attrib : this.attributes) {
			out.writeString(attrib.getName());
			out.writeDouble(attrib.getValue());
			out.writeShort(attrib.getModifiers().size());
			for(AttributeModifier mod : attrib.getModifiers()) {
				out.writeLong(mod.getUID().getMostSignificantBits());
				out.writeLong(mod.getUID().getLeastSignificantBits());
				out.writeDouble(mod.getAmount());
				out.writeByte(mod.getOperation());
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

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
