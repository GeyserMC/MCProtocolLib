package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof ItemStack)) return false;

        ItemStack that = (ItemStack) o;
        return this.id == that.id &&
                this.amount == that.amount &&
                this.data == that.data &&
                Objects.equals(this.nbt, that.nbt);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.id, this.amount, this.data, this.nbt);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
