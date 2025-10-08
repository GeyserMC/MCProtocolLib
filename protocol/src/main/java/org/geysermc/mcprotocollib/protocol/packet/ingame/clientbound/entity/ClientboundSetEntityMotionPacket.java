package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSetEntityMotionPacket implements MinecraftPacket {
    private final int entityId;
    private final Vector3d movement;

    public ClientboundSetEntityMotionPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.movement = MinecraftTypes.readLpVec3(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeLpVec3(out, this.movement);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
