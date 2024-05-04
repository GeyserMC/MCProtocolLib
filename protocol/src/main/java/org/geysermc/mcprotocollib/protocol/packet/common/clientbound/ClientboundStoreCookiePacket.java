package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundStoreCookiePacket implements MinecraftPacket {
    private final String key;
    private final byte[] payload;

    public ClientboundStoreCookiePacket(MinecraftByteBuf buf) {
        this.key = buf.readResourceLocation();
        this.payload = buf.readByteArray();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeResourceLocation(this.key);
        buf.writeByteArray(this.payload);
    }
}
