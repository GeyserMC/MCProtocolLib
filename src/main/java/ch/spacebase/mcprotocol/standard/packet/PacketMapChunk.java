package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketMapChunk extends Packet {

	public int x;
	public int z;
	public boolean groundUp;
	public int startY;
	public int endY;
	public byte data[];

	public PacketMapChunk() {
	}

	public PacketMapChunk(int x, int z, boolean groundUp, int startY, int endY, byte data[]) {
		this.x = x;
		this.z = z;
		this.groundUp = groundUp;
		this.startY = startY;
		this.endY = endY;
		this.data = data;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.x = in.readInt();
		this.z = in.readInt();
		this.groundUp = in.readBoolean();
		this.startY = in.readShort();
		this.endY = in.readShort();
		int length = in.readInt();

		byte[] compressed = in.readBytes(length);

		int off = 0;
		int msb = 0;
		for(int count = 0; count < 16; count++) {
			off += this.startY >> count & 1;
			msb += this.endY >> count & 1;
		}

		int size = (12288 * off) + (2048 * msb);
		if(this.groundUp) {
			size += 256;
		}

		this.data = new byte[size];
		Inflater inflater = new Inflater();
		inflater.setInput(compressed, 0, length);

		try {
			inflater.inflate(this.data);
		} catch(DataFormatException e) {
			throw new IOException("Bad compressed data format");
		} finally {
			inflater.end();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		Deflater deflater = new Deflater(-1);
		byte data[] = new byte[0];
		int length = 0;
		try {
			deflater.setInput(this.data, 0, this.data.length);
			deflater.finish();
			data = new byte[this.data.length];
			length = deflater.deflate(this.data);
		} finally {
			deflater.end();
		}

		out.writeInt(this.x);
		out.writeInt(this.z);
		out.writeBoolean(this.groundUp);
		out.writeShort((short) (this.startY & 0xffff));
		out.writeShort((short) (this.endY & 0xffff));
		out.writeInt(length);
		out.writeBytes(data, length);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 51;
	}

	@Override
	public void accept(PacketVisitor visitor) {
		visitor.visit(this);
	}

}
