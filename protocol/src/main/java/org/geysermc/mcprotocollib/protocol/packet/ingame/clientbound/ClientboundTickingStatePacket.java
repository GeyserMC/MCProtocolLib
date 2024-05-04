package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundTickingStatePacket implements MinecraftPacket {

    private final float tickRate;
    private final boolean isFrozen;

    public ClientboundTickingStatePacket(MinecraftByteBuf buf) {
        this.tickRate = buf.readFloat();
        this.isFrozen = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeFloat(tickRate);
        buf.writeBoolean(isFrozen);
    }
}
