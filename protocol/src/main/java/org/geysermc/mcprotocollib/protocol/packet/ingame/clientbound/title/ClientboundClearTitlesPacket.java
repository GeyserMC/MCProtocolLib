package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.title;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundClearTitlesPacket implements MinecraftPacket {
    private final boolean resetTimes;

    public ClientboundClearTitlesPacket(MinecraftByteBuf buf) {
        this.resetTimes = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeBoolean(this.resetTimes);
    }
}
