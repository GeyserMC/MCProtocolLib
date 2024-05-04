package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.VillagerTrade;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

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

    public ClientboundMerchantOffersPacket(MinecraftByteBuf buf) {
        this.containerId = buf.readVarInt();

        int size = buf.readVarInt();
        this.trades = new VillagerTrade[size];
        for (int i = 0; i < trades.length; i++) {
            ItemStack firstInput = buf.readTradeItemStack();
            ItemStack output = buf.readOptionalItemStack();
            ItemStack secondInput = buf.readNullable(buf::readTradeItemStack);

            boolean tradeDisabled = buf.readBoolean();
            int numUses = buf.readInt();
            int maxUses = buf.readInt();
            int xp = buf.readInt();
            int specialPrice = buf.readInt();
            float priceMultiplier = buf.readFloat();
            int demand = buf.readInt();

            this.trades[i] = new VillagerTrade(firstInput, secondInput, output, tradeDisabled, numUses, maxUses, xp, specialPrice, priceMultiplier, demand);
        }

        this.villagerLevel = buf.readVarInt();
        this.experience = buf.readVarInt();
        this.regularVillager = buf.readBoolean();
        this.canRestock = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.containerId);

        buf.writeVarInt(this.trades.length);
        for (VillagerTrade trade : this.trades) {
            buf.writeTradeItemStack(trade.getFirstInput());
            buf.writeOptionalItemStack(trade.getOutput());
            buf.writeNullable(trade.getSecondInput(), buf::writeTradeItemStack);

            buf.writeBoolean(trade.isTradeDisabled());
            buf.writeInt(trade.getNumUses());
            buf.writeInt(trade.getMaxUses());
            buf.writeInt(trade.getXp());
            buf.writeInt(trade.getSpecialPrice());
            buf.writeFloat(trade.getPriceMultiplier());
            buf.writeInt(trade.getDemand());
        }

        buf.writeVarInt(this.villagerLevel);
        buf.writeVarInt(this.experience);
        buf.writeBoolean(this.regularVillager);
        buf.writeBoolean(this.canRestock);
    }
}
