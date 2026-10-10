package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundMoveEntityPosRotPacket implements MinecraftPacket {
    private final int entityId;
    private final boolean onGround;
    private final int stepCount;
    private final @Nullable List<DeltaStep> steps;
    private final double moveX;
    private final double moveY;
    private final double moveZ;
    private final float yaw;
    private final float pitch;

    public ClientboundMoveEntityPosRotPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        int properties = MinecraftTypes.readVarInt(in);
        this.onGround = (properties & 1) != 0;
        this.stepCount = properties >>> 1;
        if (this.stepCount <= 0) {
            this.steps = null;
            this.moveX = in.readShort() / 4096D;
            this.moveY = in.readShort() / 4096D;
            this.moveZ = in.readShort() / 4096D;
            this.yaw = in.readByte() * 360 / 256f;
            this.pitch = in.readByte() * 360 / 256f;
            return;
        } else {
            this.moveX = 0;
            this.moveY = 0;
            this.moveZ = 0;
        }

        int maxSteps = in.readableBytes() / 7;
        if (this.stepCount > maxSteps) {
            throw new IllegalArgumentException("Number of steps " + this.stepCount + " is larger than allowed " + maxSteps);
        }

        this.steps = new ArrayList<>(this.stepCount);
        for (int i = 0; i < this.stepCount; i++) {
            int ticks = MinecraftTypes.readVarInt(in);
            double moveX = in.readShort() / 4096D;
            double moveY = in.readShort() / 4096D;
            double moveZ = in.readShort() / 4096D;
            this.steps.add(new DeltaStep(ticks, moveX, moveY, moveZ));
        }

        this.yaw = in.readByte() * 360 / 256f;
        this.pitch = in.readByte() * 360 / 256f;
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeVarInt(out, (this.onGround ? 1 : 0) | this.stepCount << 1);

        if (this.stepCount <= 0) {
            out.writeShort((int) (this.moveX * 4096));
            out.writeShort((int) (this.moveY * 4096));
            out.writeShort((int) (this.moveZ * 4096));
        } else {
            for (DeltaStep step : this.steps) {
                MinecraftTypes.writeVarInt(out, step.ticks());
                out.writeShort((int) (step.moveX() * 4096));
                out.writeShort((int) (step.moveY() * 4096));
                out.writeShort((int) (step.moveZ() * 4096));
            }
        }

        out.writeByte((byte) (this.yaw * 256 / 360));
        out.writeByte((byte) (this.pitch * 256 / 360));
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }

    public record DeltaStep(int ticks, double moveX, double moveY, double moveZ) {
    }
}
