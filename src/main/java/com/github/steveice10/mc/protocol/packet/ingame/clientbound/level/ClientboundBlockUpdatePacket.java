package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockChangeEntry;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockUpdatePacket implements MinecraftPacket {
    private final @NonNull BlockChangeEntry entry;

    public ClientboundBlockUpdatePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.entry = new BlockChangeEntry(helper.readPosition(in), helper.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writePosition(out, this.entry.getPosition());
        helper.writeVarInt(out, this.entry.getBlock());
    }
}
