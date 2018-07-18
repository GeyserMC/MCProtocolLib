package com.github.steveice10.mc.protocol.data.game.world.particle;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.util.ObjectUtil;

import java.util.Objects;

public class ItemParticleData implements ParticleData {
    private final ItemStack itemStack;

    public ItemParticleData(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof ItemParticleData)) return false;

        ItemParticleData that = (ItemParticleData) o;
        return Objects.equals(this.itemStack, that.itemStack);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.itemStack);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
