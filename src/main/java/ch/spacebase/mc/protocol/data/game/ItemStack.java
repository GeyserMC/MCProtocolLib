package ch.spacebase.mc.protocol.data.game;

import ch.spacebase.opennbt.tag.CompoundTag;

public class ItemStack {

	private int id;
	private int amount;
	private int data;
	private CompoundTag nbt;
	
	public ItemStack(int id) {
		this(id, 1);
	}
	
	public ItemStack(int id, int amount) {
		this(id, amount, 0);
	}
	
	public ItemStack(int id, int amount, int data) {
		this(id, amount, data, null);
	}
	
	public ItemStack(int id, int amount, int data, CompoundTag nbt) {
		this.id = id;
		this.amount = amount;
		this.data = data;
		this.nbt = nbt;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public int getData() {
		return this.data;
	}
	
	public CompoundTag getNBT() {
		return this.nbt;
	}
	
}
