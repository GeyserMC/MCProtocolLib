package org.geysermc.mcprotocollib.protocol.packet.status.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundPongResponsePacket implements MinecraftPacket {
    private final long pingTime;

    public ClientboundPongResponsePacket(MinecraftByteBuf buf) {
        this.pingTime = buf.readLong();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeLong(this.pingTime);
    }
}
