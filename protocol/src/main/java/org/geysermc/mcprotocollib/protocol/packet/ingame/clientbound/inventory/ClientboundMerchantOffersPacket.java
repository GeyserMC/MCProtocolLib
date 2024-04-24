package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.VillagerTrade;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

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

    public ClientboundMerchantOffersPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = helper.readVarInt(in);

        int size = helper.readVarInt(in);
        this.trades = new VillagerTrade[size];
        for (int i = 0; i < trades.length; i++) {
            ItemStack firstInput = helper.readTradeItemStack(in);
            ItemStack output = helper.readOptionalItemStack(in);
            ItemStack secondInput = null;
            if (in.readBoolean()) {
                secondInput = helper.readTradeItemStack(in);
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
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.containerId);

        helper.writeVarInt(out, this.trades.length);
        for (VillagerTrade trade : this.trades) {
            helper.writeTradeItemStack(out, trade.getFirstInput());
            helper.writeOptionalItemStack(out, trade.getOutput());
            out.writeBoolean(trade.getSecondInput() != null);
            if (trade.getSecondInput() != null) {
                helper.writeTradeItemStack(out, trade.getSecondInput());
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
