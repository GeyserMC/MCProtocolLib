package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundEntityTagQuery implements MinecraftPacket {
    private final int transactionId;
    private final int entityId;

    public ServerboundEntityTagQuery(MinecraftByteBuf buf) {
        this.transactionId = buf.readVarInt();
        this.entityId = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.transactionId);
        buf.writeVarInt(this.entityId);
    }
}
