package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ServerboundCookieResponsePacket implements MinecraftPacket {
    private final String key;
    private final byte @Nullable [] payload;

    public ServerboundCookieResponsePacket(MinecraftByteBuf buf) {
        this.key = buf.readResourceLocation();
        this.payload = buf.readNullable(buf::readByteArray);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeResourceLocation(this.key);
        buf.writeNullable(this.payload, buf::writeByteArray);
    }
}
