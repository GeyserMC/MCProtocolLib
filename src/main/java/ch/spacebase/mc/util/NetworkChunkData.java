package ch.spacebase.mc.util;

public class NetworkChunkData {
	
	private int mask;
	private int extendedMask;
	private boolean fullChunk;
	private boolean sky;
	private byte data[];
	
	public NetworkChunkData(int mask, int extendedMask, boolean fullChunk, boolean sky, byte data[]) {
		this.mask = mask;
		this.extendedMask = extendedMask;
		this.fullChunk = fullChunk;
		this.sky = sky;
		this.data = data;
	}
	
	public int getMask() {
		return this.mask;
	}

	public int getExtendedMask() {
		return this.extendedMask;
	}
	
	public boolean isFullChunk() {
		return this.fullChunk;
	}
	
	public boolean hasSkyLight() {
		return this.sky;
	}
	
	public byte[] getData() {
		return this.data;
	}
	
}
