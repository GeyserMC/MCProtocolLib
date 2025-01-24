package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@AllArgsConstructor
public class ClientboundDamageEventPacket implements MinecraftPacket {
    private final int entityId;
    private final int sourceTypeId;
    private final int sourceCauseId;
    private final int sourceDirectId;
    private final @Nullable Vector3d sourcePosition;

    public ClientboundDamageEventPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.sourceTypeId = MinecraftTypes.readVarInt(in);
        this.sourceCauseId = MinecraftTypes.readVarInt(in) - 1;
        this.sourceDirectId = MinecraftTypes.readVarInt(in) - 1;
        this.sourcePosition = in.readBoolean() ? Vector3d.from(in.readDouble(), in.readDouble(), in.readDouble()) : null;
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeVarInt(out, this.sourceTypeId);
        MinecraftTypes.writeVarInt(out, this.sourceCauseId + 1);
        MinecraftTypes.writeVarInt(out, this.sourceDirectId + 1);

        if (this.sourcePosition != null) {
            out.writeBoolean(true);
            out.writeDouble(this.sourcePosition.getX());
            out.writeDouble(this.sourcePosition.getY());
            out.writeDouble(this.sourcePosition.getZ());
        } else {
            out.writeBoolean(false);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
