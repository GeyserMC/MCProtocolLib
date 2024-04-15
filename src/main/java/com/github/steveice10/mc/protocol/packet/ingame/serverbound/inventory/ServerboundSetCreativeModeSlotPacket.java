package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.item.ItemStack;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundSetCreativeModeSlotPacket implements MinecraftPacket {
    private final short slot;
    private final @NonNull ItemStack clickedItem;

    public ServerboundSetCreativeModeSlotPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.slot = in.readShort();
        this.clickedItem = helper.readOptionalItemStack(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeShort(this.slot);
        helper.writeOptionalItemStack(out, this.clickedItem);
    }
}
