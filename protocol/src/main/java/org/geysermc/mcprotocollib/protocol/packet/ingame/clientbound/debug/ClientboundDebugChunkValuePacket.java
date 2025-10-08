package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.debug;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.debug.DebugInfo;
import org.geysermc.mcprotocollib.protocol.data.game.debug.DebugSubscriptions;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ClientboundDebugChunkValuePacket implements MinecraftPacket {
    private final int chunkX;
    private final int chunkZ;
    private final DebugSubscriptions subscriptionType;
    private final @Nullable DebugInfo subscription;

    public ClientboundDebugChunkValuePacket(ByteBuf in) {
        long chunkPos = in.readLong();
        this.chunkX = (int)chunkPos;
        this.chunkZ = (int)(chunkPos >> 32);

        this.subscriptionType = DebugSubscriptions.from(MinecraftTypes.readVarInt(in));
        this.subscription = MinecraftTypes.readDebugSubscriptionUpdate(in, this.subscriptionType);
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeLong(this.chunkX & 0xffffffffL | (this.chunkZ & 0xffffffffL) << 32);
        MinecraftTypes.writeVarInt(out, this.subscriptionType.ordinal());
        MinecraftTypes.writeDebugSubscriptionUpdate(out, this.subscriptionType, this.subscription);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
