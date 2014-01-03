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
	
	private Chunk chunks[];
	private byte biomeData[];

	public ServerChunkDataPacket() {
	}
	
	public ServerChunkDataPacket(Chunk chunks[]) {
		this(chunks, null);
	}
	
	public ServerChunkDataPacket(Chunk chunks[], byte biomeData[]) {
		if(chunks.length != 16) {
			throw new IllegalArgumentException("Chunks length must be 16.");
		}
		
		int x = 0;
		int z = 0;
		boolean hasCoords = false;
		boolean noSkylight = false;
		boolean skylight = false;
		for(int index = 0; index < chunks.length; index++) {
			if(chunks[index] != null) {
				if(!hasCoords) {
					x = chunks[index].getX();
					z = chunks[index].getZ();
					hasCoords = true;
				} else if(chunks[index].getX() != x || chunks[index].getZ() != z) {
					throw new IllegalArgumentException("Chunks must all have the same coords.");
				}
				
				if(chunks[index].getSkyLight() == null) {
					noSkylight = true;
				} else {
					skylight = true;
				}
			} else if(biomeData != null) {
				throw new IllegalArgumentException("Chunks must contain all 16 chunks in a column if biomeData is not null.");
			}
		}
		
		if(noSkylight && skylight) {
			throw new IllegalArgumentException("Either all chunks must have skylight values or none must have them.");
		}
		
		this.chunks = chunks;
		this.biomeData = biomeData;
	}
	
	public Chunk[] getChunks() {
		return this.chunks;
	}
	
	public boolean hasBiomeData() {
		return this.biomeData != null;
	}
	
	public byte[] getBiomeData() {
		return this.biomeData;
	}

	@Override
	public void read(NetInput in) throws IOException {
		// Read column data.
		int x = in.readInt();
		int z = in.readInt();
		boolean biomes = in.readBoolean();
		int chunkMask = in.readShort();
		int extendedChunkMask = in.readShort();
		byte deflated[] = in.readBytes(in.readInt());
		// Determine inflated data length.
		int chunkCount = 0;
		for(int count = 0; count < 16; count++) {
			chunkCount += chunkMask >> count & 1;
		}

		int len = 12288 * chunkCount;
		if(biomes) {
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
		ParsedChunkData chunkData = NetUtil.dataToChunks(new NetworkChunkData(x, z, chunkMask, extendedChunkMask, biomes, NetUtil.hasSky, data));
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
		out.writeInt(data.getX());
		out.writeInt(data.getZ());
		out.writeBoolean(isGroundUpContinuous());
		out.writeShort(data.getMask());
		out.writeShort(data.getExtendedMask());
		out.writeInt(len);
		out.writeBytes(deflated, len);
	}
	
	private boolean isGroundUpContinuous() {
		for (final Chunk c : chunks) {
			if (c == null) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
