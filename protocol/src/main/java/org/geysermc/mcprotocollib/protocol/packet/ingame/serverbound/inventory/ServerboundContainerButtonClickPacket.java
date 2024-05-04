package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundContainerButtonClickPacket implements MinecraftPacket {
    private final int containerId;
    private final int buttonId;

    public ServerboundContainerButtonClickPacket(MinecraftByteBuf buf) {
        this.containerId = buf.readVarInt();
        this.buttonId = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.containerId);
        buf.writeVarInt(this.buttonId);
    }
}
