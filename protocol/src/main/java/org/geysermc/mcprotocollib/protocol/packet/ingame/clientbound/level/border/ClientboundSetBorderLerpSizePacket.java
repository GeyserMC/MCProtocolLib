package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSetBorderLerpSizePacket implements MinecraftPacket {
    private final double oldSize;
    private final double newSize;
    private final long lerpTime;

    public ClientboundSetBorderLerpSizePacket(ByteBuf in) {
        this.oldSize = in.readDouble();
        this.newSize = in.readDouble();
        this.lerpTime = MinecraftTypes.readVarLong(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeDouble(this.oldSize);
        out.writeDouble(this.newSize);
        MinecraftTypes.writeVarLong(out, this.lerpTime);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
