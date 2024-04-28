package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

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
public class ClientboundDebugSamplePacket implements MinecraftPacket {
    private final long[] sample;
    private final RemoteDebugSampleType debugSampleType;

    public ClientboundDebugSamplePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.sample = helper.readLongArray(in);
        this.debugSampleType = RemoteDebugSampleType.from(helper.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeLongArray(out, this.sample);
        helper.writeVarInt(out, this.debugSampleType.ordinal());
    }
}
