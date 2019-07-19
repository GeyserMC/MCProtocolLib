package com.github.steveice10.mc.protocol.data.game.window;

import java.util.Objects;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class VillagerTrade {
    private ItemStack firstInput;
    private ItemStack secondInput;
    private ItemStack output;
    private boolean tradeDisabled;
    private int numUses;
    private int maxUses;
    private int xp;
    private int specialPrice;
    private float priceMultiplier;
    private int demand;

    public VillagerTrade(ItemStack firstInput, ItemStack secondInput, ItemStack output, boolean tradeDisabled, int numUses, int maxUses, int xp, int specialPrice, float priceMultiplier, int demand) {
        this.firstInput = firstInput;
        this.secondInput = secondInput;
        this.output = output;
        this.tradeDisabled = tradeDisabled;
        this.numUses = numUses;
        this.maxUses = maxUses;
        this.xp = xp;
        this.specialPrice = specialPrice;
        this.priceMultiplier = priceMultiplier;
        this.demand = demand;
    }

    public ItemStack getFirstInput() {
        return this.firstInput;
    }

    public ItemStack getSecondInput() {
        return this.secondInput;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public boolean isTradeDisabled() {
        return this.tradeDisabled;
    }

    public int getNumUses() {
        return this.numUses;
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public int getXp() {
        return this.xp;
    }

    public int getSpecialPrice() {
        return this.specialPrice;
    }

    public float getPriceMultiplier() {
        return this.priceMultiplier;
    }

    public int getDemand() {
        return this.demand;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof VillagerTrade)) return false;

        VillagerTrade that = (VillagerTrade) o;
        return Objects.equals(this.firstInput, that.firstInput) &&
                Objects.equals(this.secondInput, that.secondInput) &&
                Objects.equals(this.output, that.output) &&
                this.tradeDisabled == that.tradeDisabled &&
                this.numUses == that.numUses &&
                this.maxUses == that.maxUses &&
                this.xp == that.xp &&
                this.specialPrice == that.specialPrice &&
                Float.floatToIntBits(this.priceMultiplier) == Float.floatToIntBits(that.priceMultiplier) &&
                this.demand == that.demand;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.firstInput, this.secondInput, this.output, this.tradeDisabled, this.numUses, this.maxUses, this.xp, this.specialPrice, this.priceMultiplier, this.demand);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
