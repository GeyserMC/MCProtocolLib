package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.RemoteDebugSampleType;

@Data
@With
@AllArgsConstructor
public class ServerboundDebugSampleSubscriptionPacket implements MinecraftPacket {
    private final RemoteDebugSampleType debugSampleType;

    public ServerboundDebugSampleSubscriptionPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.debugSampleType = RemoteDebugSampleType.from(helper.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.debugSampleType.ordinal());
    }
}
