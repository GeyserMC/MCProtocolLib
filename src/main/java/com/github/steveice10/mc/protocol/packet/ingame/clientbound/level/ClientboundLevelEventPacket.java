package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.level.event.BonemealGrowEventData;
import com.github.steveice10.mc.protocol.data.game.level.event.BreakBlockEventData;
import com.github.steveice10.mc.protocol.data.game.level.event.BreakPotionEventData;
import com.github.steveice10.mc.protocol.data.game.level.event.ComposterEventData;
import com.github.steveice10.mc.protocol.data.game.level.event.DragonFireballEventData;
import com.github.steveice10.mc.protocol.data.game.level.event.LevelEvent;
import com.github.steveice10.mc.protocol.data.game.level.event.LevelEventData;
import com.github.steveice10.mc.protocol.data.game.level.event.ParticleEvent;
import com.github.steveice10.mc.protocol.data.game.level.event.RecordEventData;
import com.github.steveice10.mc.protocol.data.game.level.event.SmokeEventData;
import com.github.steveice10.mc.protocol.data.game.level.event.SoundEvent;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundLevelEventPacket implements Packet {
    private final @NonNull LevelEvent event;
    private final @NonNull Position position;
    private final LevelEventData data;
    private final boolean broadcast;

    public ClientboundLevelEventPacket(@NonNull LevelEvent event, @NonNull Position position, @NonNull LevelEventData data) {
        this(event, position, data, false);
    }

    public ClientboundLevelEventPacket(NetInput in) throws IOException {
        this.event = MagicValues.key(LevelEvent.class, in.readInt());
        this.position = Position.read(in);
        int value = in.readInt();
        if (this.event == SoundEvent.RECORD) {
            this.data = new RecordEventData(value);
        } else if (this.event == ParticleEvent.SMOKE) {
            this.data = MagicValues.key(SmokeEventData.class, value % 6);
        } else if (this.event == ParticleEvent.BREAK_BLOCK) {
            this.data = new BreakBlockEventData(value);
        } else if (this.event == ParticleEvent.BREAK_SPLASH_POTION) {
            this.data = new BreakPotionEventData(value);
        } else if (this.event == ParticleEvent.BONEMEAL_GROW || this.event == ParticleEvent.BONEMEAL_GROW_WITH_SOUND) {
            this.data = new BonemealGrowEventData(value);
        } else if (this.event == ParticleEvent.COMPOSTER) {
            this.data = value > 0 ? ComposterEventData.FILL_SUCCESS : ComposterEventData.FILL;
        } else if (this.event == ParticleEvent.ENDERDRAGON_FIREBALL_EXPLODE) {
            this.data = value == 1 ? DragonFireballEventData.HAS_SOUND : DragonFireballEventData.NO_SOUND;
        } else {
            this.data = null;
        }

        this.broadcast = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(MagicValues.value(Integer.class, this.event));
        Position.write(out, this.position);
        int value = 0;
        if (this.data instanceof RecordEventData) {
            value = ((RecordEventData) this.data).getRecordId();
        } else if (this.data instanceof SmokeEventData) {
            value = MagicValues.value(Integer.class, this.data);
        } else if (this.data instanceof BreakBlockEventData) {
            value = ((BreakBlockEventData) this.data).getBlockState();
        } else if (this.data instanceof BreakPotionEventData) {
            value = ((BreakPotionEventData) this.data).getPotionId();
        } else if (this.data instanceof BonemealGrowEventData) {
            value = ((BonemealGrowEventData) this.data).getParticleCount();
        } else if (this.data instanceof ComposterEventData) {
            value = MagicValues.value(Integer.class, this.data);
        } else if (this.data instanceof DragonFireballEventData) {
            value = MagicValues.value(Integer.class, this.data);
        }

        out.writeInt(value);
        out.writeBoolean(this.broadcast);
    }
}
