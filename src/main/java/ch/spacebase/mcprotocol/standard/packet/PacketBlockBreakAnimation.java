package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketBlockBreakAnimation extends Packet {

	public int entityId;
	public int x;
	public int y;
	public int z;
	public byte stage;

	public PacketBlockBreakAnimation() {
	}

	public PacketBlockBreakAnimation(int entityId, int x, int y, int z, byte stage) {
		this.entityId = entityId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.stage = stage;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
		this.stage = in.readByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
		out.writeByte(this.stage);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 55;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
