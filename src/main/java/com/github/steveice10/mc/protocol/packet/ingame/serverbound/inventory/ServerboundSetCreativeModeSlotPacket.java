package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.NotNull;

@Data
@With
@AllArgsConstructor
public class ServerboundSetCreativeModeSlotPacket implements MinecraftPacket {
    private final int slot;
    private final @NotNull ItemStack clickedItem;

    public ServerboundSetCreativeModeSlotPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.slot = in.readShort();
        this.clickedItem = helper.readItemStack(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeShort(this.slot);
        helper.writeItemStack(out, this.clickedItem);
    }
}
