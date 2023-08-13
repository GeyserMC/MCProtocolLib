package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.level.event.*;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;

import javax.annotation.Nonnull;
import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundLevelEventPacket implements MinecraftPacket {
    private final @NonNull LevelEvent event;
    private final @NonNull Vector3i position;
    private final @Nonnull LevelEventData data;
    private final boolean broadcast;

    public ClientboundLevelEventPacket(@NonNull LevelEvent event, @NonNull Vector3i position, @NonNull LevelEventData data) {
        this(event, position, data, false);
    }

    public ClientboundLevelEventPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.event = helper.readLevelEvent(in);
        this.position = helper.readPosition(in);
        int value = in.readInt();
        if (this.event instanceof LevelEventType) {
            switch ((LevelEventType) this.event) {
                case BLOCK_FIRE_EXTINGUISH:
                    this.data = FireExtinguishData.from(value);
                    break;
                case RECORD:
                    this.data = new RecordEventData(value);
                    break;
                case SMOKE:
                    this.data = new SmokeEventData(Direction.from(Math.abs(value % 6)));
                    break;
                case BREAK_BLOCK:
                case BRUSH_BLOCK_COMPLETE:
                    this.data = new BreakBlockEventData(value);
                    break;
                case BREAK_SPLASH_POTION:
                case BREAK_SPLASH_POTION2:
                    this.data = new BreakPotionEventData(value);
                    break;
                case BONEMEAL_GROW:
                case BONEMEAL_GROW_WITH_SOUND:
                    this.data = new BonemealGrowEventData(value);
                    break;
                case COMPOSTER:
                    this.data = value > 0 ? ComposterEventData.FILL_SUCCESS : ComposterEventData.FILL;
                    break;
                case ENDERDRAGON_FIREBALL_EXPLODE:
                    this.data = value == 1 ? DragonFireballEventData.HAS_SOUND : DragonFireballEventData.NO_SOUND;
                    break;
                case ELECTRIC_SPARK:
                    this.data = value >= 0 && value < 6 ? new ElectricSparkData(Direction.from(value)) : null;
                    break;
                case SCULK_BLOCK_CHARGE:
                    this.data = new SculkBlockChargeEventData(value);
                    break;
                default:
                    this.data = new UnknownLevelEventData(value);
                    break;
            }
        } else {
            this.data = new UnknownLevelEventData(value);
        }

        this.broadcast = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeLevelEvent(out, this.event);
        helper.writePosition(out, this.position);
        int value;
        if (this.data instanceof FireExtinguishData) {
            value = ((FireExtinguishData) this.data).ordinal();
        } else if (this.data instanceof RecordEventData) {
            value = ((RecordEventData) this.data).getRecordId();
        } else if (this.data instanceof SmokeEventData) {
            value = ((SmokeEventData) this.data).getDirection().ordinal();
        } else if (this.data instanceof BreakBlockEventData) {
            value = ((BreakBlockEventData) this.data).getBlockState();
        } else if (this.data instanceof BreakPotionEventData) {
            value = ((BreakPotionEventData) this.data).getPotionId();
        } else if (this.data instanceof BonemealGrowEventData) {
            value = ((BonemealGrowEventData) this.data).getParticleCount();
        } else if (this.data instanceof ComposterEventData) {
            value = ((ComposterEventData) this.data).ordinal();
        } else if (this.data instanceof DragonFireballEventData) {
            value = ((DragonFireballEventData) this.data).ordinal();
        } else if (this.data instanceof ElectricSparkData) {
            value = ((ElectricSparkData) this.data).getDirection().ordinal();
        } else if (this.data instanceof SculkBlockChargeEventData) {
            value = ((SculkBlockChargeEventData) data).getLevelValue();
        } else {
            value = ((UnknownLevelEventData) data).getData();
        }

        out.writeInt(value);
        out.writeBoolean(this.broadcast);
    }
}
