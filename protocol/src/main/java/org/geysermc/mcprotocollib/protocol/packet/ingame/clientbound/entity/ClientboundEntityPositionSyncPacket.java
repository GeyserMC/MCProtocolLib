package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundEntityPositionSyncPacket implements MinecraftPacket {
    private final int id;
    private final boolean isStepped;
    private final @Nullable Vector3d endPosition;
    private final @Nullable List<PositionStep> steps;
    private final float yRot;
    private final float xRot;
    private final boolean onGround;

    public ClientboundEntityPositionSyncPacket(ByteBuf in) {
        this.id = MinecraftTypes.readVarInt(in);
        this.isStepped = MinecraftTypes.readVarInt(in) == 1;
        if (this.isStepped) {
            this.endPosition = null;
            this.steps = MinecraftTypes.readList(in, buf -> {
                Vector3d position = Vector3d.from(buf.readDouble(), buf.readDouble(), buf.readDouble());
                int tickOffset = MinecraftTypes.readVarInt(buf);
                return new PositionStep(position, tickOffset);
            });
        } else {
            this.endPosition = Vector3d.from(in.readDouble(), in.readDouble(), in.readDouble());
            this.steps = null;
        }
        this.yRot = in.readFloat();
        this.xRot = in.readFloat();
        this.onGround = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.id);
        out.writeBoolean(this.isStepped);
        if (this.isStepped) {
            MinecraftTypes.writeList(out, this.steps, (buf, step) -> {
                buf.writeDouble(step.position.getX());
                buf.writeDouble(step.position.getY());
                buf.writeDouble(step.position.getZ());
                MinecraftTypes.writeVarInt(buf, step.tickOffset);
            });
        } else {
            out.writeDouble(this.endPosition.getX());
            out.writeDouble(this.endPosition.getY());
            out.writeDouble(this.endPosition.getZ());
        }
        out.writeFloat(this.yRot);
        out.writeFloat(this.xRot);
        out.writeBoolean(this.onGround);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }

    public record PositionStep(Vector3d position, int tickOffset) {
    }
}
