package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundAddTransientBlockPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final int block;

    public ClientboundAddTransientBlockPacket(ByteBuf in) {
        this.position = MinecraftTypes.readPosition(in);
        this.block = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, position);
        MinecraftTypes.writeVarInt(out, block);
    }
}
