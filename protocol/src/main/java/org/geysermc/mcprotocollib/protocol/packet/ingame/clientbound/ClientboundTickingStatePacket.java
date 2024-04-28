package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundTickingStatePacket implements MinecraftPacket {

    private final float tickRate;
    private final boolean isFrozen;

    public ClientboundTickingStatePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.tickRate = in.readFloat();
        this.isFrozen = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeFloat(tickRate);
        out.writeBoolean(isFrozen);
    }
}
