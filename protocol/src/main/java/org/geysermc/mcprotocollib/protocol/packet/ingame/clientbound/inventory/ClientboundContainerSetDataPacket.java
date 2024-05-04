package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.property.ContainerProperty;

@Data
@With
@AllArgsConstructor
public class ClientboundContainerSetDataPacket implements MinecraftPacket {
    private final int containerId;
    private final int rawProperty;
    private final int value;

    public ClientboundContainerSetDataPacket(int containerId, ContainerProperty rawProperty, int value) {
        this(containerId, rawProperty.ordinal(), value);
    }

    public ClientboundContainerSetDataPacket(MinecraftByteBuf buf) {
        this.containerId = buf.readUnsignedByte();
        this.rawProperty = buf.readShort();
        this.value = buf.readShort();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeShort(this.rawProperty);
        buf.writeShort(this.value);
    }
}
