package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

@Data
@With
@AllArgsConstructor
public class ServerboundSetCreativeModeSlotPacket implements MinecraftPacket {
    private final short slot;
    private final @Nullable ItemStack clickedItem;

    public ServerboundSetCreativeModeSlotPacket(MinecraftByteBuf buf) {
        this.slot = buf.readShort();
        this.clickedItem = buf.readOptionalItemStack();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeShort(this.slot);
        buf.writeOptionalItemStack(this.clickedItem);
    }
}
