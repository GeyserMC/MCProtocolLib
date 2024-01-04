package org.geysermc.mc.protocol.packet.ingame.clientbound.level.border;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundSetBorderSizePacket implements MinecraftPacket {
    private final double size;

    public ClientboundSetBorderSizePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.size = in.readDouble();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeDouble(this.size);
    }
}
