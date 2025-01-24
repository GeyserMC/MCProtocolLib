package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.RemoteDebugSampleType;

@Data
@With
@AllArgsConstructor
public class ServerboundDebugSampleSubscriptionPacket implements MinecraftPacket {
    private final RemoteDebugSampleType debugSampleType;

    public ServerboundDebugSampleSubscriptionPacket(ByteBuf in) {
        this.debugSampleType = RemoteDebugSampleType.from(MinecraftTypes.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.debugSampleType.ordinal());
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
