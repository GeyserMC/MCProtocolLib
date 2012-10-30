package ch.spacebase.mcprotocol.standard.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ItemStack {

	private short item;
	private byte stackSize;
	private short damage;
	private byte nbt[];
	
	public ItemStack() {
	}
	
	public ItemStack(short item) {
		this(item, (byte) 1);
	}
	
	public ItemStack(short item, byte stackSize) {
		this(item, stackSize, (short) 0);
	}
	
	public ItemStack(short item, byte stackSize, short damage) {
		this(item, stackSize, damage, null);
	}
	
	public ItemStack(short item, byte stackSize, short damage, byte nbt[]) {
		this.item = item;
		this.stackSize = stackSize;
		this.damage = damage;
		this.nbt = nbt;
	}
	
	public short getItem() {
		return this.item;
	}
	
	public void setItem(short item) {
		this.item = item;
	}
	
	public byte getStackSize() {
		return this.stackSize;
	}
	
	public void setStackSize(byte stackSize) {
		this.stackSize = stackSize;
	}
	
	public short getDamage() {
		return this.damage;
	}
	
	public void setDamage(short damage) {
		this.damage = damage;
	}
	
	public byte[] getNBT() {
		return this.nbt;
	}
	
	public void setNBT(byte nbt[]) {
		this.nbt = nbt;
	}
	
	public void read(DataInputStream in) throws IOException {
		this.item = in.readShort();
		if(this.item > -1) {
			this.stackSize = in.readByte();
			this.damage = in.readShort();
			short length = in.readShort();
			if(length > -1) {
				this.nbt = new byte[length];
				in.readFully(this.nbt);
			}
		}
	}
	
	public void write(DataOutputStream out) throws IOException {
		out.writeShort(this.item);
		if(this.item != -1) {
			out.writeByte(this.stackSize);
			out.writeShort(this.damage);
			if(this.nbt != null) {
				out.writeShort(this.nbt.length);
				out.write(this.nbt);
			}
		}
	}
	
}
