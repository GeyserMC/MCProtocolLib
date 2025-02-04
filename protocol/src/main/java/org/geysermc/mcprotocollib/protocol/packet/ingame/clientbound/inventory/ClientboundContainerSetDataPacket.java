package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
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

    public ClientboundContainerSetDataPacket(ByteBuf in) {
        this.containerId = MinecraftTypes.readVarInt(in);
        this.rawProperty = in.readShort();
        this.value = in.readShort();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.containerId);
        out.writeShort(this.rawProperty);
        out.writeShort(this.value);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
