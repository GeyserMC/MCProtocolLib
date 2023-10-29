package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockChangeEntry;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.NotNull;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockUpdatePacket implements MinecraftPacket {
    private final @NotNull BlockChangeEntry entry;

    public ClientboundBlockUpdatePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entry = new BlockChangeEntry(helper.readPosition(in), helper.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writePosition(out, this.entry.getPosition());
        helper.writeVarInt(out, this.entry.getBlock());
    }
}
