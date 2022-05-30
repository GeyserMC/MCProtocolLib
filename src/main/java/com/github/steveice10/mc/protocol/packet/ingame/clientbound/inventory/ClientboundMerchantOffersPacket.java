package com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.inventory.VillagerTrade;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundMerchantOffersPacket implements MinecraftPacket {
    private final int containerId;
    private final @NonNull VillagerTrade[] trades;
    private final int villagerLevel;
    private final int experience;
    private final boolean regularVillager;
    private final boolean canRestock;

    public ClientboundMerchantOffersPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.containerId = helper.readVarInt(in);

        byte size = in.readByte();
        this.trades = new VillagerTrade[size];
        for (int i = 0; i < trades.length; i++) {
            ItemStack firstInput = helper.readItemStack(in);
            ItemStack output = helper.readItemStack(in);

            ItemStack secondInput = null;
            if (in.readBoolean()) {
                secondInput = helper.readItemStack(in);
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

        this.villagerLevel = helper.readVarInt(in);
        this.experience = helper.readVarInt(in);
        this.regularVillager = in.readBoolean();
        this.canRestock = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.containerId);

        out.writeByte(this.trades.length);
        for (int i = 0; i < this.trades.length; i++) {
            VillagerTrade trade = this.trades[i];

            helper.writeItemStack(out, trade.getFirstInput());
            helper.writeItemStack(out, trade.getOutput());

            boolean hasSecondItem = trade.getSecondInput() != null;
            out.writeBoolean(hasSecondItem);
            if (hasSecondItem) {
                helper.writeItemStack(out, trade.getSecondInput());
            }

            out.writeBoolean(trade.isTradeDisabled());
            out.writeInt(trade.getNumUses());
            out.writeInt(trade.getMaxUses());
            out.writeInt(trade.getXp());
            out.writeInt(trade.getSpecialPrice());
            out.writeFloat(trade.getPriceMultiplier());
            out.writeInt(trade.getDemand());
        }

        helper.writeVarInt(out, this.villagerLevel);
        helper.writeVarInt(out, this.experience);
        out.writeBoolean(this.regularVillager);
        out.writeBoolean(this.canRestock);
    }
}
