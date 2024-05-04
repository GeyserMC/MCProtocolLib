package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundBlockEntityTagQueryPacket implements MinecraftPacket {
    private final int transactionId;
    private final @NonNull Vector3i position;

    public ServerboundBlockEntityTagQueryPacket(MinecraftByteBuf buf) {
        this.transactionId = buf.readVarInt();
        this.position = buf.readPosition();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.transactionId);
        buf.writePosition(this.position);
    }
}
