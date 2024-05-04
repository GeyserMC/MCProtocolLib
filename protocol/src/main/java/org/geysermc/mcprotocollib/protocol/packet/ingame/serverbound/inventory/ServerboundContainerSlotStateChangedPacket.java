package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundContainerSlotStateChangedPacket implements MinecraftPacket {

    private final int slotId;
    private final int containerId;
    private final boolean newState;

    public ServerboundContainerSlotStateChangedPacket(MinecraftByteBuf buf) {
        this.slotId = buf.readVarInt();
        this.containerId = buf.readVarInt();
        this.newState = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.slotId);
        buf.writeVarInt(this.containerId);
        buf.writeBoolean(this.newState);
    }
}
