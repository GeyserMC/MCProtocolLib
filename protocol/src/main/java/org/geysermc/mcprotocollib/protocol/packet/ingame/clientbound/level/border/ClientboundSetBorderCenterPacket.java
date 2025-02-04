package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import io.netty.buffer.ByteBuf;
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

    public ClientboundSetBorderCenterPacket(ByteBuf in) {
        this.newCenterX = in.readDouble();
        this.newCenterZ = in.readDouble();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeDouble(this.newCenterX);
        out.writeDouble(this.newCenterZ);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
