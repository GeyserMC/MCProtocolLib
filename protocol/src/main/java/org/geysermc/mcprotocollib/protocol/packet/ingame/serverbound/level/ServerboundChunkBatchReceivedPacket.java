package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundChunkBatchReceivedPacket implements MinecraftPacket {
    private final float desiredChunksPerTick;

    public ServerboundChunkBatchReceivedPacket(MinecraftByteBuf buf) {
        this.desiredChunksPerTick = buf.readFloat();
    }

    public void serialize(MinecraftByteBuf buf) {
        buf.writeFloat(this.desiredChunksPerTick);
    }
}
