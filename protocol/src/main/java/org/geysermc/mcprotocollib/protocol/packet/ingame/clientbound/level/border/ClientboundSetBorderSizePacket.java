package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetBorderSizePacket implements MinecraftPacket {
    private final double size;

    public ClientboundSetBorderSizePacket(MinecraftByteBuf buf) {
        this.size = buf.readDouble();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeDouble(this.size);
    }
}
