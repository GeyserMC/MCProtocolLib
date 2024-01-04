package org.geysermc.mc.protocol.packet.ingame.serverbound.level;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ServerboundChunkBatchReceivedPacket implements MinecraftPacket {
    private final float desiredChunksPerTick;

    public ServerboundChunkBatchReceivedPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.desiredChunksPerTick = in.readFloat();
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeFloat(this.desiredChunksPerTick);
    }
}
