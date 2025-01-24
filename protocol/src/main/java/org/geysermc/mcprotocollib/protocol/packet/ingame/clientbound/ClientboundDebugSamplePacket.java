package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

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
public class ClientboundDebugSamplePacket implements MinecraftPacket {
    private final long[] sample;
    private final RemoteDebugSampleType debugSampleType;

    public ClientboundDebugSamplePacket(ByteBuf in) {
        this.sample = MinecraftTypes.readLongArray(in);
        this.debugSampleType = RemoteDebugSampleType.from(MinecraftTypes.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeLongArray(out, this.sample);
        MinecraftTypes.writeVarInt(out, this.debugSampleType.ordinal());
    }
}
