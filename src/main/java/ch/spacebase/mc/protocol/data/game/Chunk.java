package ch.spacebase.mc.protocol.data.game;

public class Chunk {

	private int x;
	private int z;
	
	private byte blocks[];
	private NibbleArray metadata;
	private NibbleArray blocklight;
	private NibbleArray skylight;
	private NibbleArray extendedBlocks;
	
	public Chunk(int x, int z, byte blocks[], NibbleArray metadata, NibbleArray blocklight, NibbleArray skylight, NibbleArray extendedBlocks) {
		this.x = x;
		this.z = z;
		this.blocks = blocks;
		this.metadata = metadata;
		this.blocklight = blocklight;
		this.skylight = skylight;
		this.extendedBlocks = extendedBlocks;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public byte[] getBlocks() {
		return this.blocks;
	}
	
	public NibbleArray getMetadata() {
		return this.metadata;
	}
	
	public NibbleArray getBlockLight() {
		return this.blocklight;
	}
	
	public NibbleArray getSkyLight() {
		return this.skylight;
	}
	
	public NibbleArray getExtendedBlocks() {
		return this.extendedBlocks;
	}

	public void deleteExtendedBlocks() {
		this.extendedBlocks = null;
	}

	public boolean isEmpty() {
		for(byte block : this.blocks) {
			if(block != 0) {
				return false;
			}
		}
		
		return true;
	}
	
}
