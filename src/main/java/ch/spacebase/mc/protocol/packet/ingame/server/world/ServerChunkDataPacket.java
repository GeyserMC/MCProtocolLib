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

public class ServerChunkDataPacket implements Packet {
	
	private int x;
	private int z;
	private Chunk chunks[];
	private byte biomeData[];

	@SuppressWarnings("unused")
	private ServerChunkDataPacket() {
	}
	
	/**
	 * Convenience constructor for creating a packet to unload chunks.
	 * @param x X of the chunk column.
	 * @param z Z of the chunk column.
	 */
	public ServerChunkDataPacket(int x, int z) {
		this(x, z, new Chunk[16], new byte[256]);
	}
	
	/**
	 * Constructs a ServerChunkDataPacket for updating chunks.
	 * @param x X of the chunk column.
	 * @param z Z of the chunk column.
	 * @param chunks Array of chunks in the column. Length must be 16 but can contain null values.
	 * @throws IllegalArgumentException If the chunk array length is not 16 or skylight arrays exist in some but not all chunks.
	 */
	public ServerChunkDataPacket(int x, int z, Chunk chunks[]) {
		this(x, z, chunks, null);
	}
	
	/**
	 * Constructs a ServerChunkDataPacket for updating a full column of chunks.
	 * @param x X of the chunk column.
	 * @param z Z of the chunk column.
	 * @param chunks Array of chunks in the column. Length must be 16 but can contain null values.
	 * @param biomeData Array of biome data for the column.
	 * @throws IllegalArgumentException If the chunk array length is not 16 or skylight arrays exist in some but not all chunks.
	 */
	public ServerChunkDataPacket(int x, int z, Chunk chunks[], byte biomeData[]) {
		if(chunks.length != 16) {
			throw new IllegalArgumentException("Chunks length must be 16.");
		}
		
		boolean noSkylight = false;
		boolean skylight = false;
		for(int index = 0; index < chunks.length; index++) {
			if(chunks[index] != null) {
				if(chunks[index].getSkyLight() == null) {
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
	
	public int getX() {
		return this.x;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public Chunk[] getChunks() {
		return this.chunks;
	}
	
	public byte[] getBiomeData() {
		return this.biomeData;
	}
	
	public boolean isFullChunk() {
		return this.biomeData != null;
	}

	@Override
	public void read(NetInput in) throws IOException {
		// Read column data.
		this.x = in.readInt();
		this.z = in.readInt();
		boolean fullChunk = in.readBoolean();
		int chunkMask = in.readShort();
		int extendedChunkMask = in.readShort();
		byte deflated[] = in.readBytes(in.readInt());
		// Determine inflated data length.
		int chunkCount = 0;
		for(int count = 0; count < 16; count++) {
			chunkCount += chunkMask >> count & 1;
		}

		int len = 12288 * chunkCount;
		if(fullChunk) {
			len += 256;
		}

		byte data[] = new byte[len];
		// Inflate chunk data.
		Inflater inflater = new Inflater();
		inflater.setInput(deflated, 0, deflated.length);
		try {
			inflater.inflate(data);
		} catch(DataFormatException e) {
			throw new IOException("Bad compressed data format");
		} finally {
			inflater.end();
		}

		// Parse data into chunks and biome data.
		ParsedChunkData chunkData = NetUtil.dataToChunks(new NetworkChunkData(chunkMask, extendedChunkMask, fullChunk, false, data));
		this.chunks = chunkData.getChunks();
		this.biomeData = chunkData.getBiomes();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		// Parse chunks into data.
		NetworkChunkData data = NetUtil.chunksToData(new ParsedChunkData(this.chunks, this.biomeData));
		// Deflate chunk data.
		Deflater deflater = new Deflater(-1);
		byte deflated[] = new byte[data.getData().length];
		int len = data.getData().length;
		try {
			deflater.setInput(data.getData(), 0, data.getData().length);
			deflater.finish();
			len = deflater.deflate(deflated);
		} finally {
			deflater.end();
		}
		
		// Write data to the network.
		out.writeInt(this.x);
		out.writeInt(this.z);
		out.writeBoolean(data.isFullChunk());
		out.writeShort(data.getMask());
		out.writeShort(data.getExtendedMask());
		out.writeInt(len);
		out.writeBytes(deflated, len);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
