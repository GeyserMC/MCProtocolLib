package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.ResourceLocation;

@Data
@With
@AllArgsConstructor
public class ClientboundCookieRequestPacket implements MinecraftPacket {
    private final ResourceLocation key;

    public ClientboundCookieRequestPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.key = helper.readResourceLocation(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeResourceLocation(out, this.key);
    }
}
