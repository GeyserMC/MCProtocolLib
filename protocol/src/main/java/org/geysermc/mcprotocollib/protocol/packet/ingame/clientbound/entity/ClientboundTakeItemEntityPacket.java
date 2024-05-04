package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundTakeItemEntityPacket implements MinecraftPacket {
    private final int collectedEntityId;
    private final int collectorEntityId;
    private final int itemCount;

    public ClientboundTakeItemEntityPacket(MinecraftByteBuf buf) {
        this.collectedEntityId = buf.readVarInt();
        this.collectorEntityId = buf.readVarInt();
        this.itemCount = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.collectedEntityId);
        buf.writeVarInt(this.collectorEntityId);
        buf.writeVarInt(this.itemCount);
    }
}
