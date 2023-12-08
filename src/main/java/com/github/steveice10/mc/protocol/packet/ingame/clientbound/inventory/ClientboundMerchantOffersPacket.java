package com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.inventory.VillagerTrade;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.NotNull;

@Data
@With
@AllArgsConstructor
public class ClientboundMerchantOffersPacket implements MinecraftPacket {
    private final int containerId;
    private final @NotNull VillagerTrade[] trades;
    private final int villagerLevel;
    private final int experience;
    private final boolean regularVillager;
    private final boolean canRestock;

    public ClientboundMerchantOffersPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = helper.readVarInt(in);

        int size = helper.readVarInt(in);
        this.trades = new VillagerTrade[size];
        for (int i = 0; i < trades.length; i++) {
            ItemStack firstInput = helper.readItemStack(in);
            ItemStack output = helper.readItemStack(in);
            ItemStack secondInput = helper.readItemStack(in);

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
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.containerId);

        helper.writeVarInt(out, this.trades.length);
        for (VillagerTrade trade : this.trades) {
            helper.writeItemStack(out, trade.firstInput());
            helper.writeItemStack(out, trade.output());
            helper.writeItemStack(out, trade.secondInput());

            out.writeBoolean(trade.tradeDisabled());
            out.writeInt(trade.numUses());
            out.writeInt(trade.maxUses());
            out.writeInt(trade.xp());
            out.writeInt(trade.specialPrice());
            out.writeFloat(trade.priceMultiplier());
            out.writeInt(trade.demand());
        }

        helper.writeVarInt(out, this.villagerLevel);
        helper.writeVarInt(out, this.experience);
        out.writeBoolean(this.regularVillager);
        out.writeBoolean(this.canRestock);
    }
}
