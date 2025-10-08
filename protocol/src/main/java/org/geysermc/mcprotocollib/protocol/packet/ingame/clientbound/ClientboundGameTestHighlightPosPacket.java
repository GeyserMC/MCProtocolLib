package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.debug.DebugInfo;
import org.geysermc.mcprotocollib.protocol.data.game.debug.DebugSubscriptions;

@Data
@With
@AllArgsConstructor
public class ClientboundGameTestHighlightPosPacket implements MinecraftPacket {
    private final Vector3i absolutePos;
    private final Vector3i relativePos;

    public ClientboundGameTestHighlightPosPacket(ByteBuf in) {
        this.absolutePos = MinecraftTypes.readPosition(in);
        this.relativePos = MinecraftTypes.readPosition(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.absolutePos);
        MinecraftTypes.writePosition(out, this.relativePos);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
