package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetBorderCenterPacket implements MinecraftPacket {
    private final double newCenterX;
    private final double newCenterZ;

    public ClientboundSetBorderCenterPacket(MinecraftByteBuf buf) {
        this.newCenterX = buf.readDouble();
        this.newCenterZ = buf.readDouble();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeDouble(this.newCenterX);
        buf.writeDouble(this.newCenterZ);
    }
}
