package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

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
public class ClientboundSetDefaultSpawnPositionPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final float angle;

    public ClientboundSetDefaultSpawnPositionPacket(ByteBuf in) {
        this.position = MinecraftTypes.readPosition(in);
        this.angle = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.position);
        out.writeFloat(this.angle);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
