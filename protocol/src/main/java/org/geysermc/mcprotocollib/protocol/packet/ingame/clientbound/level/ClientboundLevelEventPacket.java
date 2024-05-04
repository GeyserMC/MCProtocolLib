package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.BonemealGrowEventData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.BreakBlockEventData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.BreakPotionEventData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.ComposterEventData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.DragonFireballEventData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.ElectricSparkData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.FireExtinguishData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.LevelEvent;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.LevelEventData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.LevelEventType;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.RecordEventData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.SculkBlockChargeEventData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.SmokeEventData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.TrialSpawnerDetectEventData;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.UnknownLevelEventData;

@Data
@With
@AllArgsConstructor
public class ClientboundLevelEventPacket implements MinecraftPacket {
    private final @NonNull LevelEvent event;
    private final @NonNull Vector3i position;
    private final @NonNull LevelEventData data;
    private final boolean broadcast;

    public ClientboundLevelEventPacket(@NonNull LevelEvent event, @NonNull Vector3i position, @NonNull LevelEventData data) {
        this(event, position, data, false);
    }

    public ClientboundLevelEventPacket(MinecraftByteBuf buf) {
        this.event = buf.readLevelEvent();
        this.position = buf.readPosition();
        int value = buf.readInt();
        if (this.event instanceof LevelEventType levelEventType) {
            switch (levelEventType) {
                case BLOCK_FIRE_EXTINGUISH -> this.data = FireExtinguishData.from(value);
                case RECORD -> this.data = new RecordEventData(value);
                case SMOKE, WHITE_SMOKE -> this.data = new SmokeEventData(Direction.from(Math.abs(value % 6)));
                case BREAK_BLOCK, BRUSH_BLOCK_COMPLETE -> this.data = new BreakBlockEventData(value);
                case BREAK_SPLASH_POTION, BREAK_SPLASH_POTION2 -> this.data = new BreakPotionEventData(value);
                case BONEMEAL_GROW, BONEMEAL_GROW_WITH_SOUND -> this.data = new BonemealGrowEventData(value);
                case COMPOSTER -> this.data = value > 0 ? ComposterEventData.FILL_SUCCESS : ComposterEventData.FILL;
                case ENDERDRAGON_FIREBALL_EXPLODE -> this.data = value == 1 ? DragonFireballEventData.HAS_SOUND : DragonFireballEventData.NO_SOUND;
                case ELECTRIC_SPARK -> this.data = value >= 0 && value < 6 ? new ElectricSparkData(Direction.from(value)) : new UnknownLevelEventData(value);
                case SCULK_BLOCK_CHARGE -> this.data = new SculkBlockChargeEventData(value);
                case TRIAL_SPAWNER_DETECT_PLAYER -> this.data = new TrialSpawnerDetectEventData(value);
                default -> this.data = new UnknownLevelEventData(value);
            }
        } else {
            this.data = new UnknownLevelEventData(value);
        }

        this.broadcast = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeLevelEvent(this.event);
        buf.writePosition(this.position);
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

        buf.writeInt(value);
        buf.writeBoolean(this.broadcast);
    }
}
