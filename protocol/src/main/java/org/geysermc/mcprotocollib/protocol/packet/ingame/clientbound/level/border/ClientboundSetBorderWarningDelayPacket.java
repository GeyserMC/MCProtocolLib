package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetBorderWarningDelayPacket implements MinecraftPacket {
    private final int warningDelay;

    public ClientboundSetBorderWarningDelayPacket(MinecraftByteBuf buf) {
        this.warningDelay = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.warningDelay);
    }
}
