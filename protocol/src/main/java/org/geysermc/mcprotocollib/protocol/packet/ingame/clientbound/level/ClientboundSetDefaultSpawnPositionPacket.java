package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

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
public class ClientboundSetDefaultSpawnPositionPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final float angle;

    public ClientboundSetDefaultSpawnPositionPacket(MinecraftByteBuf buf) {
        this.position = buf.readPosition();
        this.angle = buf.readFloat();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writePosition(this.position);
        buf.writeFloat(this.angle);
    }
}
