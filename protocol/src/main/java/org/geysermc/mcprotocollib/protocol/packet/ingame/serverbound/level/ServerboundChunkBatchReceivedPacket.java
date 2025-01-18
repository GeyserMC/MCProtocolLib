package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundChunkBatchReceivedPacket implements MinecraftPacket {
    private final float desiredChunksPerTick;

    public ServerboundChunkBatchReceivedPacket(ByteBuf in) {
        this.desiredChunksPerTick = in.readFloat();
    }

    public void serialize(ByteBuf out) {
        out.writeFloat(this.desiredChunksPerTick);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
