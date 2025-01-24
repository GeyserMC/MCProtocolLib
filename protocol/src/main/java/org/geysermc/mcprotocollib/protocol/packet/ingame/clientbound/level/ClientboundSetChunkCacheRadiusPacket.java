package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSetChunkCacheRadiusPacket implements MinecraftPacket {
    private final int viewDistance;

    public ClientboundSetChunkCacheRadiusPacket(ByteBuf in) {
        this.viewDistance = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.viewDistance);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
