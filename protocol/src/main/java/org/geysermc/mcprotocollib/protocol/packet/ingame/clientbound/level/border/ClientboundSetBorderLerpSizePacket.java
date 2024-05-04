package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetBorderLerpSizePacket implements MinecraftPacket {
    private final double oldSize;
    private final double newSize;
    private final long lerpTime;

    public ClientboundSetBorderLerpSizePacket(MinecraftByteBuf buf) {
        this.oldSize = buf.readDouble();
        this.newSize = buf.readDouble();
        this.lerpTime = buf.readVarLong();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeDouble(this.oldSize);
        buf.writeDouble(this.newSize);
        buf.writeVarLong(this.lerpTime);
    }
}
