package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketSpawnVehicle extends Packet {

	public int entityId;
	public byte type;
	public int x;
	public int y;
	public int z;
	public int data;
	public short speedX;
	public short speedY;
	public short speedZ;
	
	public PacketSpawnVehicle() {
	}
	
	public PacketSpawnVehicle(int entityId, byte type, int x, int y, int z, int data, short speedX, short speedY, short speedZ) {
		this.entityId = entityId;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
		this.speedX = speedX;
		this.speedY = speedY;
		this.speedZ = speedZ;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.type = in.readByte();
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
		this.data = in.readInt();
		if(this.data > 0) {
			this.speedX = in.readShort();
			this.speedY = in.readShort();
			this.speedZ = in.readShort();
		}
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.type);
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
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
	
}
