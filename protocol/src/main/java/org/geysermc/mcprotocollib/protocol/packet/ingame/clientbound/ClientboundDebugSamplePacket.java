package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.RemoteDebugSampleType;

@Data
@With
@AllArgsConstructor
public class ClientboundDebugSamplePacket implements MinecraftPacket {
    private final long[] sample;
    private final RemoteDebugSampleType debugSampleType;

    public ClientboundDebugSamplePacket(MinecraftByteBuf buf) {
        this.sample = buf.readLongArray();
        this.debugSampleType = RemoteDebugSampleType.from(buf.readVarInt());
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeLongArray(this.sample);
        buf.writeVarInt(this.debugSampleType.ordinal());
    }
}
