package org.spacehq.mc.protocol.data.game;

import java.util.Arrays;

public class ShortArray3d {

	private short[] data;

	public ShortArray3d(int size) {
		this.data = new short[size];
	}

	public ShortArray3d(short[] array) {
		this.data = array;
	}

	public short[] getData() {
		return this.data;
	}

	public int get(int x, int y, int z) {
		return this.data[y << 8 | z << 4 | x] & 0xFFFF;
	}

	public void set(int x, int y, int z, int val) {
		this.data[y << 8 | z << 4 | x] = (short) val;
	}

	public void fill(int val) {
		Arrays.fill(this.data, (short) val);
	}

}
