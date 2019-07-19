package com.github.steveice10.mc.protocol.packet.ingame.server.window;

import java.io.IOException;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.VillagerTrade;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ServerTradeListPacket extends MinecraftPacket {

    private int windowId;
    private VillagerTrade[] trades;
    private int villagerLevel;
    private int experience;
    private boolean isRegularVillager;
    private boolean canRestock;

    public ServerTradeListPacket() {
    }

    public ServerTradeListPacket(int windowId, VillagerTrade[] trades, int villagerLevel, int experience, boolean isRegularVillager, boolean canRestock) {
        this.windowId = windowId;
        this.trades = trades;
        this.villagerLevel = villagerLevel;
        this.experience = experience;
        this.isRegularVillager = isRegularVillager;
        this.canRestock = canRestock;
    }

    public int getWindowId() {
        return windowId;
    }

    public VillagerTrade[] getTrades() {
        return trades;
    }

    public int getVillagerLevel() {
        return villagerLevel;
    }

    public int getExperience() {
        return experience;
    }

    public boolean isRegularVillager() {
        return isRegularVillager;
    }

    public boolean canRestock() {
        return canRestock;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readVarInt();

        byte size = in.readByte();
        this.trades = new VillagerTrade[size];
        for(int i = 0; i < trades.length; i++) {
            ItemStack firstInput = NetUtil.readItem(in);
            ItemStack output = NetUtil.readItem(in);

            ItemStack secondInput = null;
            if(in.readBoolean()) {
                secondInput = NetUtil.readItem(in);
            }

            boolean tradeDisabled = in.readBoolean();
            int numUses = in.readInt();
            int maxUses = in.readInt();
            int xp = in.readInt();
            int specialPrice = in.readInt();
            float priceMultiplier = in.readFloat();
            int demand = in.readInt();

            this.trades[i] = new VillagerTrade(firstInput, secondInput, output, tradeDisabled, numUses, maxUses, xp, specialPrice, priceMultiplier, demand);
        }

        this.villagerLevel = in.readVarInt();
        this.experience = in.readVarInt();
        this.isRegularVillager = in.readBoolean();
        this.canRestock = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.windowId);

        out.writeByte(this.trades.length);
        for(int i = 0; i < this.trades.length; i++) {
            VillagerTrade trade = this.trades[i];

            NetUtil.writeItem(out, trade.getFirstInput());
            NetUtil.writeItem(out, trade.getOutput());

            boolean hasSecondItem = trade.getSecondInput() != null;
            out.writeBoolean(hasSecondItem);
            if(hasSecondItem) {
                NetUtil.writeItem(out, trade.getSecondInput());
            }

            out.writeBoolean(trade.isTradeDisabled());
            out.writeInt(trade.getNumUses());
            out.writeInt(trade.getMaxUses());
            out.writeInt(trade.getXp());
            out.writeInt(trade.getSpecialPrice());
            out.writeFloat(trade.getPriceMultiplier());
            out.writeInt(trade.getDemand());
        }

        out.writeVarInt(this.villagerLevel);
        out.writeVarInt(this.experience);
        out.writeBoolean(this.isRegularVillager);
        out.writeBoolean(this.canRestock);
    }
}
