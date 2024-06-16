package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundStoreCookiePacket implements MinecraftPacket {
    private final Key key;
    private final byte[] payload;

    public ClientboundStoreCookiePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.key = helper.readResourceLocation(in);
        this.payload = helper.readByteArray(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeResourceLocation(out, this.key);
        helper.writeByteArray(out, this.payload);
    }
}
