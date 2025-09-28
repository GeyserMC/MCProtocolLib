package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.debug;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.debug.DebugInfo;
import org.geysermc.mcprotocollib.protocol.data.game.debug.DebugSubscriptions;

@Data
@With
@AllArgsConstructor
public class ClientboundDebugEventPacket implements MinecraftPacket {
    private final DebugSubscriptions subscriptionType;
    private final DebugInfo subscription;

    public ClientboundDebugEventPacket(ByteBuf in) {
        this.subscriptionType = DebugSubscriptions.from(MinecraftTypes.readVarInt(in));
        this.subscription = MinecraftTypes.readDebugSubscription(in, this.subscriptionType);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.subscriptionType.ordinal());
        MinecraftTypes.writeDebugSubscription(out, this.subscriptionType, this.subscription);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
