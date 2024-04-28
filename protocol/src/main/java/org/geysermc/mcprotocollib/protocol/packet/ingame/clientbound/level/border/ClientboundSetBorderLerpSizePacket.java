package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetBorderLerpSizePacket implements MinecraftPacket {
    private final double oldSize;
    private final double newSize;
    private final long lerpTime;

    public ClientboundSetBorderLerpSizePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.oldSize = in.readDouble();
        this.newSize = in.readDouble();
        this.lerpTime = helper.readVarLong(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeDouble(this.oldSize);
        out.writeDouble(this.newSize);
        helper.writeVarLong(out, this.lerpTime);
    }
}
