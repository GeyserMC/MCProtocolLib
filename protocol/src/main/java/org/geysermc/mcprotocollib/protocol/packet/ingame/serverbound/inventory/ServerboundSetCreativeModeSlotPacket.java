package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

@Data
@With
@AllArgsConstructor
public class ServerboundSetCreativeModeSlotPacket implements MinecraftPacket {
    private final short slot;
    private final @Nullable ItemStack clickedItem;

    public ServerboundSetCreativeModeSlotPacket(ByteBuf in) {
        this.slot = in.readShort();
        this.clickedItem = MinecraftTypes.readOptionalItemStack(in, true);
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeShort(this.slot);
        MinecraftTypes.writeOptionalItemStack(out, this.clickedItem, true);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
