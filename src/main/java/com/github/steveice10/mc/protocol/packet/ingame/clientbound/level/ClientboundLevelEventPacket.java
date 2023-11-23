package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.level.event.*;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.jetbrains.annotations.NotNull;

@Data
@With
@AllArgsConstructor
public class ClientboundLevelEventPacket implements MinecraftPacket {
    private final @NotNull LevelEvent event;
    private final @NotNull Vector3i position;
    private final @NotNull LevelEventData data;
    private final boolean broadcast;

    public ClientboundLevelEventPacket(@NotNull LevelEvent event, @NotNull Vector3i position, @NotNull LevelEventData data) {
        this(event, position, data, false);
    }

    public ClientboundLevelEventPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.event = helper.readLevelEvent(in);
        this.position = helper.readPosition(in);
        int value = in.readInt();
        if (this.event instanceof LevelEventType levelEventType) {
            switch (levelEventType) {
                case BLOCK_FIRE_EXTINGUISH -> this.data = FireExtinguishData.from(value);
                case RECORD -> this.data = new RecordEventData(value);
                case SMOKE -> this.data = new SmokeEventData(Direction.from(Math.abs(value % 6)));
                case BREAK_BLOCK, BRUSH_BLOCK_COMPLETE -> this.data = new BreakBlockEventData(value);
                case BREAK_SPLASH_POTION, BREAK_SPLASH_POTION2 -> this.data = new BreakPotionEventData(value);
                case BONEMEAL_GROW, BONEMEAL_GROW_WITH_SOUND -> this.data = new BonemealGrowEventData(value);
                case COMPOSTER -> this.data = value > 0 ? ComposterEventData.FILL_SUCCESS : ComposterEventData.FILL;
                case ENDERDRAGON_FIREBALL_EXPLODE -> this.data = value == 1 ? DragonFireballEventData.HAS_SOUND : DragonFireballEventData.NO_SOUND;
                case ELECTRIC_SPARK -> // TODO: Look into why this is null (field is marked as @NotNull)
                    this.data = value >= 0 && value < 6 ? new ElectricSparkData(Direction.from(value)) : null;
                case SCULK_BLOCK_CHARGE -> this.data = new SculkBlockChargeEventData(value);
                default -> this.data = new UnknownLevelEventData(value);
            }
        } else {
            this.data = new UnknownLevelEventData(value);
        }

        this.broadcast = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeLevelEvent(out, this.event);
        helper.writePosition(out, this.position);
        int value;
        if (this.data instanceof FireExtinguishData) {
            value = ((FireExtinguishData) this.data).ordinal();
        } else if (this.data instanceof RecordEventData) {
            value = ((RecordEventData) this.data).recordId();
        } else if (this.data instanceof SmokeEventData) {
            value = ((SmokeEventData) this.data).direction().ordinal();
        } else if (this.data instanceof BreakBlockEventData) {
            value = ((BreakBlockEventData) this.data).blockState();
        } else if (this.data instanceof BreakPotionEventData) {
            value = ((BreakPotionEventData) this.data).potionId();
        } else if (this.data instanceof BonemealGrowEventData) {
            value = ((BonemealGrowEventData) this.data).particleCount();
        } else if (this.data instanceof ComposterEventData) {
            value = ((ComposterEventData) this.data).ordinal();
        } else if (this.data instanceof DragonFireballEventData) {
            value = ((DragonFireballEventData) this.data).ordinal();
        } else if (this.data instanceof ElectricSparkData) {
            value = ((ElectricSparkData) this.data).direction().ordinal();
        } else if (this.data instanceof SculkBlockChargeEventData) {
            value = ((SculkBlockChargeEventData) data).getLevelValue();
        } else {
            value = ((UnknownLevelEventData) data).data();
        }

        out.writeInt(value);
        out.writeBoolean(this.broadcast);
    }
}
