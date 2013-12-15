package ch.spacebase.mc.util;

public class NetworkChunkData {
	
	private int x;
	private int z;
	private int mask;
	private int extendedMask;
	private boolean biomes;
	private boolean sky;
	private byte data[];
	
	public NetworkChunkData(int x, int z, int mask, int extendedMask, boolean biomes, boolean sky, byte data[]) {
		this.x = x;
		this.z = z;
		this.mask = mask;
		this.extendedMask = extendedMask;
		this.biomes = biomes;
		this.sky = sky;
		this.data = data;
	}
	
	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}
	
	public int getMask() {
		return this.mask;
	}

	public int getExtendedMask() {
		return this.extendedMask;
	}
	
	public boolean hasBiomes() {
		return this.biomes;
	}
	
	public boolean hasSkyLight() {
		return this.sky;
	}
	
	public byte[] getData() {
		return this.data;
	}
	
}
