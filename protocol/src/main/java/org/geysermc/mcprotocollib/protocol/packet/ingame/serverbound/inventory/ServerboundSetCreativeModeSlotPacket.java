package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@With
@AllArgsConstructor
public class ServerboundSetCreativeModeSlotPacket implements MinecraftPacket {
    private final short slot;
    private final @Nullable ItemStack clickedItem;

    public ServerboundSetCreativeModeSlotPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.slot = in.readShort();
        this.clickedItem = helper.readOptionalItemStack(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeShort(this.slot);
        helper.writeOptionalItemStack(out, this.clickedItem);
    }
}
