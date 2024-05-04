package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundContainerClosePacket implements MinecraftPacket {
    private final int containerId;

    public ClientboundContainerClosePacket(MinecraftByteBuf buf) {
        this.containerId = buf.readUnsignedByte();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeByte(this.containerId);
    }
}
