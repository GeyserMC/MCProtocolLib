package com.github.steveice10.mc.protocol.data.game.window;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.util.ReflectionToString;

import java.util.Objects;

public class PrepareCraftingGridEntry {
    private ItemStack item;
    private byte craftingGridSlot;
    private byte playerInventorySlot;

    public PrepareCraftingGridEntry(ItemStack item, byte craftingGridSlot, byte playerInventorySlot) {
        this.item = item;
        this.craftingGridSlot = craftingGridSlot;
        this.playerInventorySlot = playerInventorySlot;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public byte getCraftingGridSlot() {
        return this.craftingGridSlot;
    }

    public byte getPlayerInventorySlot() {
        return this.playerInventorySlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrepareCraftingGridEntry)) return false;
        PrepareCraftingGridEntry that = (PrepareCraftingGridEntry) o;
        return this.craftingGridSlot == that.craftingGridSlot &&
                this.playerInventorySlot == that.playerInventorySlot &&
                Objects.equals(this.item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, craftingGridSlot, playerInventorySlot);
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}
