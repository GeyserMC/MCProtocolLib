package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetChunkCacheRadiusPacket implements MinecraftPacket {
    private final int viewDistance;

    public ClientboundSetChunkCacheRadiusPacket(MinecraftByteBuf buf) {
        this.viewDistance = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.viewDistance);
    }
}
