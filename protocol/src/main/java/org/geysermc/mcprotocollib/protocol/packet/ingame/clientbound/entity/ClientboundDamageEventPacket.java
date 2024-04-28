package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@AllArgsConstructor
public class ClientboundDamageEventPacket implements MinecraftPacket {
    private final int entityId;
    private final int sourceTypeId;
    private final int sourceCauseId;
    private final int sourceDirectId;
    private final @Nullable Vector3d sourcePosition;

    public ClientboundDamageEventPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entityId = helper.readVarInt(in);
        this.sourceTypeId = helper.readVarInt(in);
        this.sourceCauseId = helper.readVarInt(in) - 1;
        this.sourceDirectId = helper.readVarInt(in) - 1;
        this.sourcePosition = in.readBoolean() ? Vector3d.from(in.readDouble(), in.readDouble(), in.readDouble()) : null;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.entityId);
        helper.writeVarInt(out, this.sourceTypeId);
        helper.writeVarInt(out, this.sourceCauseId + 1);
        helper.writeVarInt(out, this.sourceDirectId + 1);

        if (this.sourcePosition != null) {
            out.writeBoolean(true);
            out.writeDouble(this.sourcePosition.getX());
            out.writeDouble(this.sourcePosition.getY());
            out.writeDouble(this.sourcePosition.getZ());
        } else {
            out.writeBoolean(false);
        }
    }
}
