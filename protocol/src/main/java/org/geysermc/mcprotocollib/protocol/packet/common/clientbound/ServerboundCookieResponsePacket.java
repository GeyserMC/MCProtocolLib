package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ServerboundCookieResponsePacket implements MinecraftPacket {
    private final String key;
    private final byte @Nullable[] payload;

    public ServerboundCookieResponsePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.key = helper.readResourceLocation(in);
        this.payload = helper.readNullable(in, helper::readByteArray);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeResourceLocation(out, this.key);
        helper.writeNullable(out, this.payload, helper::writeByteArray);
    }
}
