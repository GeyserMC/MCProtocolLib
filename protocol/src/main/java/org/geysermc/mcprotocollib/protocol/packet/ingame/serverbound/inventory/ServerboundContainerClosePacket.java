package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundContainerClosePacket implements MinecraftPacket {
    private final int containerId;

    public ServerboundContainerClosePacket(MinecraftByteBuf buf) {
        this.containerId = buf.readByte();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeByte(this.containerId);
    }
}
