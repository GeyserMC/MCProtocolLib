package com.github.steveice10.mc.protocol.packet.ingame.server.window;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.VillagerTrade;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerTradeListPacket implements Packet {
    private int windowId;
    private @NonNull VillagerTrade[] trades;
    private int villagerLevel;
    private int experience;
    private boolean regularVillager;
    private boolean canRestock;

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readVarInt();

        byte size = in.readByte();
        this.trades = new VillagerTrade[size];
        for (int i = 0; i < trades.length; i++) {
            ItemStack firstInput = ItemStack.read(in);
            ItemStack output = ItemStack.read(in);

            ItemStack secondInput = null;
            if (in.readBoolean()) {
                secondInput = ItemStack.read(in);
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
        this.regularVillager = in.readBoolean();
        this.canRestock = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.windowId);

        out.writeByte(this.trades.length);
        for (int i = 0; i < this.trades.length; i++) {
            VillagerTrade trade = this.trades[i];

            ItemStack.write(out, trade.getFirstInput());
            ItemStack.write(out, trade.getOutput());

            boolean hasSecondItem = trade.getSecondInput() != null;
            out.writeBoolean(hasSecondItem);
            if (hasSecondItem) {
                ItemStack.write(out, trade.getSecondInput());
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
        out.writeBoolean(this.regularVillager);
        out.writeBoolean(this.canRestock);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
