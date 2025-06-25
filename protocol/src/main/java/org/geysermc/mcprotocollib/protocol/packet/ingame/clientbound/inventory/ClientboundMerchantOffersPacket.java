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

import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundMerchantOffersPacket implements MinecraftPacket {
    private final int containerId;
    private final @NonNull List<VillagerTrade> offers;
    private final int villagerLevel;
    private final int villagerXp;
    private final boolean showProgress;
    private final boolean canRestock;

    public ClientboundMerchantOffersPacket(ByteBuf in) {
        this.containerId = MinecraftTypes.readVarInt(in);

        this.offers = MinecraftTypes.readList(in, input -> {
            VillagerTrade.ItemCost baseCostA = MinecraftTypes.readItemCost(in);
            ItemStack result = MinecraftTypes.readItemStack(in);
            VillagerTrade.ItemCost costB = MinecraftTypes.readNullable(in, MinecraftTypes::readItemCost);

            boolean outOfStock = in.readBoolean();
            int uses = in.readInt();
            int maxUses = in.readInt();
            int xp = in.readInt();
            int specialPriceDiff = in.readInt();
            float priceMultiplier = in.readFloat();
            int demand = in.readInt();

            return new VillagerTrade(baseCostA, result, costB, outOfStock, uses, maxUses, xp, specialPriceDiff, priceMultiplier, demand);
        });

        this.villagerLevel = MinecraftTypes.readVarInt(in);
        this.villagerXp = MinecraftTypes.readVarInt(in);
        this.showProgress = in.readBoolean();
        this.canRestock = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.containerId);

        MinecraftTypes.writeList(out, this.offers, (output, offer) -> {
            MinecraftTypes.writeItemCost(out, offer.getItemCostA());
            MinecraftTypes.writeItemStack(out, offer.getResult());
            MinecraftTypes.writeNullable(out, offer.getItemCostB(), MinecraftTypes::writeItemCost);

            out.writeBoolean(offer.isOutOfStock());
            out.writeInt(offer.getUses());
            out.writeInt(offer.getMaxUses());
            out.writeInt(offer.getXp());
            out.writeInt(offer.getSpecialPriceDiff());
            out.writeFloat(offer.getPriceMultiplier());
            out.writeInt(offer.getDemand());
        });

        MinecraftTypes.writeVarInt(out, this.villagerLevel);
        MinecraftTypes.writeVarInt(out, this.villagerXp);
        out.writeBoolean(this.showProgress);
        out.writeBoolean(this.canRestock);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
