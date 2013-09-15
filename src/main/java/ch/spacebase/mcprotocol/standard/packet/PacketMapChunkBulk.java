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

public class PacketMapChunkBulk extends Packet {

	public boolean skylight;
	public int columnX[];
	public int columnZ[];
	public int primary[];
	public int add[];
	public byte compressed[];
	public byte chunks[][];
	public int length;

	public PacketMapChunkBulk() {
	}

	public PacketMapChunkBulk(int columnX[], int columnZ[], int primary[], int add[], byte data[], byte chunks[][], boolean skylight) {
		this.columnX = columnX;
		this.columnZ = columnZ;
		this.primary = primary;
		this.add = add;
		this.skylight = skylight;

		Deflater deflater = new Deflater(-1);

		try {
			deflater.setInput(data, 0, data.length);
			deflater.finish();
			this.compressed = new byte[data.length];
			this.length = deflater.deflate(this.compressed);
		} finally {
			deflater.end();
		}

		this.chunks = chunks;
	}

	@Override
	public void read(NetInput in) throws IOException {
		short columns = in.readShort();
		this.length = in.readInt();
		this.skylight = in.readBoolean();
		this.columnX = new int[columns];
		this.columnZ = new int[columns];
		this.primary = new int[columns];
		this.add = new int[columns];
		this.chunks = new byte[columns][];

		this.compressed = in.readBytes(this.length);
		byte decompressed[] = new byte[0x30100 * columns];
		Inflater inflater = new Inflater();
		inflater.setInput(this.compressed, 0, this.length);

		try {
			inflater.inflate(decompressed);
		} catch (DataFormatException e) {
			throw new IOException("Bad compressed data format");
		} finally {
			inflater.end();
		}

		int currlen = 0;
		for(int column = 0; column < columns; column++) {
			this.columnX[column] = in.readInt();
			this.columnZ[column] = in.readInt();
			this.primary[column] = in.readShort();
			this.add[column] = in.readShort();
			int off = 0;

			for(int count = 0; count < 16; count++) {
				off += this.primary[column] >> count & 1;
			}

			int datalen = 2048 * (5 * off) + 256;
			this.chunks[column] = new byte[datalen];
			System.arraycopy(decompressed, currlen, this.chunks[column], 0, datalen);
			currlen += datalen;
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeShort(this.columnX.length);
		out.writeInt(this.length);
		out.writeBoolean(this.skylight);
		out.writeBytes(this.compressed, this.length);
		for(int count = 0; count < this.columnX.length; count++) {
			out.writeInt(this.columnX[count]);
			out.writeInt(this.columnZ[count]);
			out.writeShort(this.primary[count]);
			out.writeShort(this.add[count]);
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
		return 56;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
