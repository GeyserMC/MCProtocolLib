package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketLightning extends Packet {

	public int entityId;
	public boolean unk;
	public int x;
	public int y;
	public int z;

	public PacketLightning() {
	}

	public PacketLightning(int entityId, boolean unk, int x, int y, int z) {
		this.entityId = entityId;
		this.unk = unk;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.unk = in.readBoolean();
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeBoolean(this.unk);
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 71;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
