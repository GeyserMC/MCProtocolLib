package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.RemoteDebugSampleType;

@Data
@With
@AllArgsConstructor
public class ServerboundDebugSampleSubscriptionPacket implements MinecraftPacket {
    private final RemoteDebugSampleType debugSampleType;

    public ServerboundDebugSampleSubscriptionPacket(MinecraftByteBuf buf) {
        this.debugSampleType = RemoteDebugSampleType.from(buf.readVarInt());
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.debugSampleType.ordinal());
    }
}
