package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@AllArgsConstructor
public class ClientboundDamageEventPacket implements MinecraftPacket {
    private final int entityId;
    private final int sourceTypeId;
    private final int sourceCauseId;
    private final int sourceDirectId;
    private final @Nullable Vector3d sourcePosition;

    public ClientboundDamageEventPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.sourceTypeId = buf.readVarInt();
        this.sourceCauseId = buf.readVarInt() - 1;
        this.sourceDirectId = buf.readVarInt() - 1;
        this.sourcePosition = buf.readBoolean() ? Vector3d.from(buf.readDouble(), buf.readDouble(), buf.readDouble()) : null;
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeVarInt(this.sourceTypeId);
        buf.writeVarInt(this.sourceCauseId + 1);
        buf.writeVarInt(this.sourceDirectId + 1);

        if (this.sourcePosition != null) {
            buf.writeBoolean(true);
            buf.writeDouble(this.sourcePosition.getX());
            buf.writeDouble(this.sourcePosition.getY());
            buf.writeDouble(this.sourcePosition.getZ());
        } else {
            buf.writeBoolean(false);
        }
    }
}
