package org.geysermc.mc.protocol.packet.ingame.clientbound.level;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundSetChunkCacheRadiusPacket implements MinecraftPacket {
    private final int viewDistance;

    public ClientboundSetChunkCacheRadiusPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.viewDistance = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.viewDistance);
    }
}
