package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
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

    public ClientboundMerchantOffersPacket(ByteBuf in) {
        this.containerId = MinecraftTypes.readVarInt(in);

        int size = MinecraftTypes.readVarInt(in);
        this.trades = new VillagerTrade[size];
        for (int i = 0; i < trades.length; i++) {
            ItemStack firstInput = MinecraftTypes.readTradeItemStack(in);
            ItemStack output = MinecraftTypes.readOptionalItemStack(in);
            ItemStack secondInput = MinecraftTypes.readNullable(in, MinecraftTypes::readTradeItemStack);

            boolean tradeDisabled = in.readBoolean();
            int numUses = in.readInt();
            int maxUses = in.readInt();
            int xp = in.readInt();
            int specialPrice = in.readInt();
            float priceMultiplier = in.readFloat();
            int demand = in.readInt();

            this.trades[i] = new VillagerTrade(firstInput, secondInput, output, tradeDisabled, numUses, maxUses, xp, specialPrice, priceMultiplier, demand);
        }

        this.villagerLevel = MinecraftTypes.readVarInt(in);
        this.experience = MinecraftTypes.readVarInt(in);
        this.regularVillager = in.readBoolean();
        this.canRestock = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.containerId);

        MinecraftTypes.writeVarInt(out, this.trades.length);
        for (VillagerTrade trade : this.trades) {
            MinecraftTypes.writeTradeItemStack(out, trade.getFirstInput());
            MinecraftTypes.writeOptionalItemStack(out, trade.getOutput());
            MinecraftTypes.writeNullable(out, trade.getSecondInput(), MinecraftTypes::writeTradeItemStack);

            out.writeBoolean(trade.isTradeDisabled());
            out.writeInt(trade.getNumUses());
            out.writeInt(trade.getMaxUses());
            out.writeInt(trade.getXp());
            out.writeInt(trade.getSpecialPrice());
            out.writeFloat(trade.getPriceMultiplier());
            out.writeInt(trade.getDemand());
        }

        MinecraftTypes.writeVarInt(out, this.villagerLevel);
        MinecraftTypes.writeVarInt(out, this.experience);
        out.writeBoolean(this.regularVillager);
        out.writeBoolean(this.canRestock);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
