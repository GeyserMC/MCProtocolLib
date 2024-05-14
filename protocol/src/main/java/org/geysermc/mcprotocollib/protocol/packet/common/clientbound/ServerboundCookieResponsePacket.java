package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ServerboundCookieResponsePacket implements MinecraftPacket {
    private final Key key;
    private final byte @Nullable [] payload;

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
