package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketMapChunk extends Packet {

	public int x;
	public int z;
	public boolean groundUp;
	public int startY;
	public int endY;
	public byte data[];
	public int length;
	
	public PacketMapChunk() {
	}
	
	public PacketMapChunk(int x, int z, boolean groundUp, int startY, int endY, byte data[]) {
		this.x = x;
		this.z = z;
		this.groundUp = groundUp;
		this.startY	= startY;
		this.endY = endY;
		
        Deflater deflater = new Deflater(-1);

        try {
            deflater.setInput(data, 0, data.length);
            deflater.finish();
            this.data = new byte[data.length];
            this.length = deflater.deflate(this.data);
        } finally {
            deflater.end();
        }
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		this.x = in.readInt();
		this.z = in.readInt();
		this.groundUp = in.readBoolean();
		this.startY = in.readShort();
		this.endY = in.readShort();
		this.length = in.readInt();
		
		byte[] compressed = new byte[this.length];
		in.readFully(compressed, 0, this.length);
		
		int off = 0;
		for (int count = 0; count < 16; count++) {
			off += this.startY >> count & 1;
		}

		int size = 12288 * off;
		if (this.groundUp) {
			size += 256;
		}

		this.data = new byte[size];
		Inflater inflater = new Inflater();
		inflater.setInput(compressed, 0, this.length);

		try {
			inflater.inflate(this.data);
		} catch (DataFormatException e) {
			throw new IOException("Bad compressed data format");
		} finally {
			inflater.end();
		}
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.x);
		out.writeInt(this.z);
		out.writeBoolean(this.groundUp);
		out.writeShort((short) (this.startY & 0xffff));
		out.writeShort((short) (this.endY & 0xffff));
		out.writeByte(this.length);
		out.write(this.data, 0, this.length);
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
	
}
