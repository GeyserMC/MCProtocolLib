package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketNamedSound extends Packet {

	public String sound;
	public int x;
	public int y;
	public int z;
	public float volume;
	public int pitch;
	
	public PacketNamedSound() {
	}
	
	public PacketNamedSound(String sound, int x, byte y, int z, float volume, int pitch) {
		this.sound = sound;
		this.x = x;
		this.y = y;
		this.z = z;
		this.volume = volume;
		this.pitch = pitch;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.sound = IOUtils.readString(in);
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
		this.volume = in.readFloat();
		this.pitch = in.readUnsignedByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		IOUtils.writeString(out, this.sound);
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
		out.writeFloat(this.volume);
		out.writeByte(this.pitch);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 62;
	}
	
}
