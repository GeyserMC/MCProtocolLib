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
public class ClientboundDebugEntityValuePacket implements MinecraftPacket {
    private final int entityId;
    private final DebugSubscriptions subscriptionType;
    private final @Nullable DebugInfo subscription;

    public ClientboundDebugEntityValuePacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.subscriptionType = DebugSubscriptions.from(MinecraftTypes.readVarInt(in));
        this.subscription = MinecraftTypes.readDebugSubscriptionUpdate(in, this.subscriptionType);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeVarInt(out, this.subscriptionType.ordinal());
        MinecraftTypes.writeDebugSubscriptionUpdate(out, this.subscriptionType, this.subscription);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
