package com.github.steveice10.mc.protocol.data.game.window;

import java.io.IOException;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class VillagerTrade {

    private ItemStack input1Item;
    private ItemStack input2Item;
    private ItemStack outputItem;
    private boolean tradeDisabled;
    private int numberOfUses;
    private int maxNumberOfUses;
    private int xp;
    private int specialPrice;
    private float priceMultiplier;

    public VillagerTrade() {
    }

    public VillagerTrade(ItemStack input1Item, ItemStack input2Item, ItemStack outputItem, boolean tradeDisabled, int numberOfUses, int maxNumberOfUses, int xp, int specialPrice, float priceMultiplier) {
        this.input1Item = input1Item;
        this.input2Item = input2Item;
        this.outputItem = outputItem;
        this.tradeDisabled = tradeDisabled;
        this.numberOfUses = numberOfUses;
        this.maxNumberOfUses = maxNumberOfUses;
        this.xp = xp;
        this.specialPrice = specialPrice;
        this.priceMultiplier = priceMultiplier;
    }

    public ItemStack getInput1Item() {
        return input1Item;
    }

    public ItemStack getInput2Item() {
        return input2Item;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public boolean isTradeDisabled() {
        return tradeDisabled;
    }

    public int getNumberOfUses() {
        return numberOfUses;
    }

    public int getMaxNumberOfUses() {
        return maxNumberOfUses;
    }

    public int getXp() {
        return xp;
    }

    public int getSpecialPrice() {
        return specialPrice;
    }

    public float getPriceMultiplier() {
        return priceMultiplier;
    }

    public void read(NetInput in) throws IOException {
        this.input1Item = NetUtil.readItem(in);
        this.outputItem = NetUtil.readItem(in);
        boolean hasSecondItem = in.readBoolean();
        if (hasSecondItem) {
            this.input2Item = NetUtil.readItem(in);
        }
        this.tradeDisabled = in.readBoolean();
        this.numberOfUses = in.readInt();
        this.maxNumberOfUses = in.readInt();
        this.xp = in.readInt();
        this.specialPrice = in.readInt();
        this.priceMultiplier = in.readFloat();
    }

    public void write(NetOutput out) throws IOException {
        NetUtil.writeItem(out, this.input1Item);
        NetUtil.writeItem(out, this.outputItem);
        boolean hasSecondItem = this.input2Item != null;
        out.writeBoolean(hasSecondItem);
        if (hasSecondItem) {
            NetUtil.writeItem(out, this.input2Item);
        }
        out.writeBoolean(this.tradeDisabled);
        out.writeInt(this.numberOfUses);
        out.writeInt(this.maxNumberOfUses);
        out.writeInt(this.xp);
        out.writeInt(this.specialPrice);
        out.writeFloat(this.priceMultiplier);
    }
}
