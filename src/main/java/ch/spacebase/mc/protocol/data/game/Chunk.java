package ch.spacebase.mc.protocol.data.game;

public class Chunk {

	private ByteArray3d blocks;
	private NibbleArray3d metadata;
	private NibbleArray3d blocklight;
	private NibbleArray3d skylight;
	private NibbleArray3d extendedBlocks;
	
	public Chunk(boolean skylight, boolean extended) {
		this(new ByteArray3d(4096), new NibbleArray3d(4096), new NibbleArray3d(4096), skylight ? new NibbleArray3d(4096) : null, extended ? new NibbleArray3d(4096) : null);
	}
	
	public Chunk(ByteArray3d blocks, NibbleArray3d metadata, NibbleArray3d blocklight, NibbleArray3d skylight, NibbleArray3d extendedBlocks) {
		this.blocks = blocks;
		this.metadata = metadata;
		this.blocklight = blocklight;
		this.skylight = skylight;
		this.extendedBlocks = extendedBlocks;
	}
	
	public ByteArray3d getBlocks() {
		return this.blocks;
	}
	
	public NibbleArray3d getMetadata() {
		return this.metadata;
	}
	
	public NibbleArray3d getBlockLight() {
		return this.blocklight;
	}
	
	public NibbleArray3d getSkyLight() {
		return this.skylight;
	}
	
	public NibbleArray3d getExtendedBlocks() {
		return this.extendedBlocks;
	}

	public void deleteExtendedBlocks() {
		this.extendedBlocks = null;
	}

	public boolean isEmpty() {
		for(byte block : this.blocks.getData()) {
			if(block != 0) {
				return false;
			}
		}
		
		return true;
	}
	
}
