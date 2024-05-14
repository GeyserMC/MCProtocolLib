package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ServerboundCookieResponsePacket implements MinecraftPacket {
    private final ResourceLocation key;
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
