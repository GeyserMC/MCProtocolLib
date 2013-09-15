package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketSpawnObject extends Packet {

	public int entityId;
	public byte type;
	public int x;
	public int y;
	public int z;
	public byte pitch;
	public byte yaw;
	public int data;
	public short speedX;
	public short speedY;
	public short speedZ;

	public PacketSpawnObject() {
	}

	public PacketSpawnObject(int entityId, byte type, int x, int y, int z, byte yaw, byte pitch, int data, short speedX, short speedY, short speedZ) {
		this.entityId = entityId;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.data = data;
		this.speedX = speedX;
		this.speedY = speedY;
		this.speedZ = speedZ;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.type = in.readByte();
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
		this.pitch = in.readByte();
		this.yaw = in.readByte();
		this.data = in.readInt();
		if(this.data > 0) {
			this.speedX = in.readShort();
			this.speedY = in.readShort();
			this.speedZ = in.readShort();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.type);
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
		out.writeByte(this.pitch);
		out.writeByte(this.yaw);
		out.writeInt(this.data);
		if(this.data > 0) {
			out.writeShort(this.speedX);
			out.writeShort(this.speedY);
			out.writeShort(this.speedZ);
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
		return 23;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
