package org.geysermc.mcprotocollib.protocol.packet.status.serverbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPingRequestPacket implements MinecraftPacket {
    private final long pingTime;

    public ServerboundPingRequestPacket(MinecraftByteBuf buf) {
        this.pingTime = buf.readLong();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeLong(this.pingTime);
    }
}
