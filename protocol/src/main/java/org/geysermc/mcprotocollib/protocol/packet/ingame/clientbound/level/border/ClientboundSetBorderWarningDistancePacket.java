package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetBorderWarningDistancePacket implements MinecraftPacket {
    private final int warningBlocks;

    public ClientboundSetBorderWarningDistancePacket(MinecraftByteBuf buf) {
        this.warningBlocks = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.warningBlocks);
    }
}
