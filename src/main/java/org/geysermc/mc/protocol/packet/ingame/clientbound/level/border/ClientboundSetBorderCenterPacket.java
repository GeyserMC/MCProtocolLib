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
public class ClientboundSetBorderCenterPacket implements MinecraftPacket {
    private final double newCenterX;
    private final double newCenterZ;

    public ClientboundSetBorderCenterPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.newCenterX = in.readDouble();
        this.newCenterZ = in.readDouble();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeDouble(this.newCenterX);
        out.writeDouble(this.newCenterZ);
    }
}
