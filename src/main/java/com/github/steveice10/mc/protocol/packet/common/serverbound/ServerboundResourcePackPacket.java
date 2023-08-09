package com.github.steveice10.mc.protocol.packet.common.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.ResourcePackStatus;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundResourcePackPacket implements MinecraftPacket {
    private final @NonNull ResourcePackStatus status;

    public ServerboundResourcePackPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.status = ResourcePackStatus.from(helper.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.status.ordinal());
    }
}
