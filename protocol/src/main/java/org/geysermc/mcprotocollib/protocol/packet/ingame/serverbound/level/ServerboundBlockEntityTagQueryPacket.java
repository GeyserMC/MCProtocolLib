package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundBlockEntityTagQueryPacket implements MinecraftPacket {
    private final int transactionId;
    private final @NonNull Vector3i position;

    public ServerboundBlockEntityTagQueryPacket(ByteBuf in) {
        this.transactionId = MinecraftTypes.readVarInt(in);
        this.position = MinecraftTypes.readPosition(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.transactionId);
        MinecraftTypes.writePosition(out, this.position);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
