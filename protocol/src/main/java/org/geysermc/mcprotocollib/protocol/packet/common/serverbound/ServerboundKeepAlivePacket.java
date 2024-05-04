package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundKeepAlivePacket implements MinecraftPacket {
    private final long pingId;

    public ServerboundKeepAlivePacket(MinecraftByteBuf buf) {
        this.pingId = buf.readLong();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeLong(this.pingId);
    }

}
