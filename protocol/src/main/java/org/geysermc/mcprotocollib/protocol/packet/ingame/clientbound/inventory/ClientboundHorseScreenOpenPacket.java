package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundHorseScreenOpenPacket implements MinecraftPacket {
    private final int containerId;
    private final int numberOfSlots;
    private final int entityId;

    public ClientboundHorseScreenOpenPacket(MinecraftByteBuf buf) {
        this.containerId = buf.readByte();
        this.numberOfSlots = buf.readVarInt();
        this.entityId = buf.readInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeVarInt(this.numberOfSlots);
        buf.writeInt(this.entityId);
    }
}
