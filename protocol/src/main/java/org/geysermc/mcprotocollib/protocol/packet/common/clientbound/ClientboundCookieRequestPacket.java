package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundCookieRequestPacket implements MinecraftPacket {
    private final String key;

    public ClientboundCookieRequestPacket(MinecraftByteBuf buf) {
        this.key = buf.readResourceLocation();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeResourceLocation(this.key);
    }
}
