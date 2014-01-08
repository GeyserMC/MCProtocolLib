package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import ch.spacebase.mc.protocol.data.game.Chunk;
import ch.spacebase.mc.util.ParsedChunkData;
import ch.spacebase.mc.util.NetworkChunkData;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerMultiChunkDataPacket implements Packet {

	private int x[];
	private int z[];
	private Chunk chunks[][];
	private byte biomeData[][];

	@SuppressWarnings("unused")
	private ServerMultiChunkDataPacket() {
	}

	public ServerMultiChunkDataPacket(int x[], int z[], Chunk chunks[][], byte biomeData[][]) {
		if(biomeData == null) {
			throw new IllegalArgumentException("BiomeData cannot be null.");
		}
		
		if(x.length != chunks.length || z.length != chunks.length) {
			throw new IllegalArgumentException("X, Z, and Chunk arrays must be equal in length.");
		}

		boolean noSkylight = false;
		boolean skylight = false;
		for(int index = 0; index < chunks.length; index++) {
			Chunk column[] = chunks[index];
			if(column.length != 16) {
				throw new IllegalArgumentException("Chunk columns must contain 16 chunks each.");
			}

			for(int y = 0; y < column.length; y++) {
				if(column[y] == null) {
					throw new IllegalArgumentException("Chunk column must contain all 16 chunks.");
				}
				
				if(column[y].getSkyLight() == null) {
					noSkylight = true;
				} else {
					skylight = true;
				}
			}
		}

		if(noSkylight && skylight) {
			throw new IllegalArgumentException("Either all chunks must have skylight values or none must have them.");
		}

		this.x = x;
		this.z = z;
		this.chunks = chunks;
		this.biomeData = biomeData;
	}

	public int getColumns() {
		return this.chunks.length;
	}
	
	public int getX(int column) {
		return this.x[column];
	}
	
	public int getZ(int column) {
		return this.z[column];
	}

	public Chunk[] getChunks(int column) {
		return this.chunks[column];
	}

	public byte[] getBiomeData(int column) {
		return this.biomeData[column];
	}

	@Override
	public void read(NetInput in) throws IOException {
		// Read packet base data.
		short columns = in.readShort();
		int deflatedLength = in.readInt();
		boolean skylight = in.readBoolean();
		byte deflatedBytes[] = in.readBytes(deflatedLength);
		// Inflate chunk data.
		byte[] inflated = new byte[196864 * columns];
		Inflater inflater = new Inflater();
		inflater.setInput(deflatedBytes, 0, deflatedLength);
		try {
			inflater.inflate(inflated);
		} catch(DataFormatException e) {
			throw new IOException("Bad compressed data format");
		} finally {
			inflater.end();
		}

		this.x = new int[columns];
		this.z = new int[columns];
		this.chunks = new Chunk[columns][];
		this.biomeData = new byte[columns][];
		// Cycle through and read all columns.
		int pos = 0;
		for(int count = 0; count < columns; count++) {
			// Read column-specific data.
			int x = in.readInt();
			int z = in.readInt();
			int chunkMask = in.readShort();
			int extendedChunkMask = in.readShort();
			// Determine column data length.
			int chunks = 0;
			int extended = 0;
			for(int ch = 0; ch < 16; ch++) {
				chunks += chunkMask >> ch & 1;
				extended += extendedChunkMask >> ch & 1;
			}

			int length = (8192 * chunks + 256) + (2048 * extended);
			if(skylight) {
				length += 2048 * chunks;
			}

			// Copy column data into a new array.
			byte dat[] = new byte[length];
			System.arraycopy(inflated, pos, dat, 0, length);
			// Read data into chunks and biome data.
			ParsedChunkData chunkData = NetUtil.dataToChunks(new NetworkChunkData(chunkMask, extendedChunkMask, true, skylight, dat));
			this.x[count] = x;
			this.z[count] = z;
			this.chunks[count] = chunkData.getChunks();
			this.biomeData[count] = chunkData.getBiomes();
			pos += length;
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		// Prepare chunk data arrays.
		int chunkMask[] = new int[this.chunks.length];
		int extendedChunkMask[] = new int[this.chunks.length];
		// Determine values to be written by cycling through columns.
		int pos = 0;
		byte bytes[] = new byte[0];
		boolean skylight = false;
		for(int count = 0; count < this.chunks.length; ++count) {
			Chunk column[] = this.chunks[count];
			// Convert chunks into network data.
			NetworkChunkData netData = NetUtil.chunksToData(new ParsedChunkData(column, this.biomeData[count]));
			if(bytes.length < pos + netData.getData().length) {
				byte[] newArray = new byte[pos + netData.getData().length];
				System.arraycopy(bytes, 0, newArray, 0, bytes.length);
				bytes = newArray;
			}
			
			if(netData.hasSkyLight()) {
				skylight = true;
			}

			// Copy column data into data array.
			System.arraycopy(netData.getData(), 0, bytes, pos, netData.getData().length);
			pos += netData.getData().length;
			// Set column-specific values.
			chunkMask[count] = netData.getMask();
			extendedChunkMask[count] = netData.getExtendedMask();
		}

		// Deflate chunk data.
		Deflater deflater = new Deflater(-1);
		byte deflatedData[] = new byte[pos];
		int deflatedLength = pos;
		try {
			deflater.setInput(bytes, 0, pos);
			deflater.finish();
			deflatedLength = deflater.deflate(deflatedData);
		} finally {
			deflater.end();
		}

		// Write data to the network.
		out.writeShort(this.chunks.length);
		out.writeInt(deflatedLength);
		out.writeBoolean(skylight);
		out.writeBytes(deflatedData, deflatedLength);
		for(int count = 0; count < this.chunks.length; ++count) {
			out.writeInt(this.x[count]);
			out.writeInt(this.z[count]);
			out.writeShort((short) (chunkMask[count] & 65535));
			out.writeShort((short) (extendedChunkMask[count] & 65535));
		}
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
