package ch.spacebase.mcprotocol.standard.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A stack of items.
 */
public class ItemStack {

	/**
	 * Id of the items in the stack.
	 */
	private short item;
	
	/**
	 * The size of the item stack.
	 */
	private byte stackSize;
	
	/**
	 * The damage value of the items in the stack.
	 */
	private short damage;
	
	/**
	 * The compressed NBT data of the item stack.
	 */
	private byte nbt[];

	/**
	 * Creates a new, empty item stack for reading to.
	 */
	public ItemStack() {
	}

	/**
	 * Creates an item stack with the given id.
	 * @param item Item id of the stack.
	 */
	public ItemStack(short item) {
		this(item, (byte) 1);
	}

	/**
	 * Creates an item stack with the given id and size.
	 * @param item Item id of the stack.
	 * @param stackSize Size of the stack.
	 */
	public ItemStack(short item, byte stackSize) {
		this(item, stackSize, (short) 0);
	}

	/**
	 * Creates an item stack with the given id, size, and damage value.
	 * @param item Item id of the stack.
	 * @param stackSize Size of the stack.
	 * @param damage Damage value of the stack.
	 */
	public ItemStack(short item, byte stackSize, short damage) {
		this(item, stackSize, damage, null);
	}

	/**
	 * Creates an item stack with the given id, size, damage value, and compressed NBT data.
	 * @param item Item id of the stack.
	 * @param stackSize Size of the stack.
	 * @param damage Damage value of the stack.
	 * @param nbt Compressed NBT data of the stack.
	 */
	public ItemStack(short item, byte stackSize, short damage, byte nbt[]) {
		this.item = item;
		this.stackSize = stackSize;
		this.damage = damage;
		this.nbt = nbt;
	}

	/**
	 * Gets the item id of the stack.
	 * @return The stack's item id.
	 */
	public short getItem() {
		return this.item;
	}
	
	/**
	 * Sets the item id of the stack.
	 * @param item New item id of the stack.
	 */
	public void setItem(short item) {
		this.item = item;
	}

	/**
	 * Gets the size of the stack.
	 * @return The stack's size.
	 */
	public byte getStackSize() {
		return this.stackSize;
	}

	/**
	 * Sets the size of the stack.
	 * @param item New size of the stack.
	 */
	public void setStackSize(byte stackSize) {
		this.stackSize = stackSize;
	}

	/**
	 * Gets the damage value of the stack.
	 * @return The stack's damage value.
	 */
	public short getDamage() {
		return this.damage;
	}

	/**
	 * Sets the damage value of the stack.
	 * @param item New damage value of the stack.
	 */
	public void setDamage(short damage) {
		this.damage = damage;
	}

	/**
	 * Gets the compressed NBT data of the stack.
	 * @return The stack's compressed NBT data.
	 */
	public byte[] getNBT() {
		return this.nbt;
	}

	/**
	 * Sets the compressed NBT data of the stack.
	 * @param item New compressed NBT data of the stack.
	 */
	public void setNBT(byte nbt[]) {
		this.nbt = nbt;
	}

	/**
	 * Reads data from the given input stream into this item stack.
	 * @param in Input stream to read from.
	 * @throws IOException If an I/O error occurs.
	 */
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

	/**
	 * Writes this item stack to the given output stream.
	 * @param out Output stream to write to.
	 * @throws IOException If an I/O error occurs.
	 */
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
