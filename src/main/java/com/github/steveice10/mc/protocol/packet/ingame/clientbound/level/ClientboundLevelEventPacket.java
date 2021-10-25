package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.level.effect.BonemealGrowEffectData;
import com.github.steveice10.mc.protocol.data.game.level.effect.BreakBlockEffectData;
import com.github.steveice10.mc.protocol.data.game.level.effect.BreakPotionEffectData;
import com.github.steveice10.mc.protocol.data.game.level.effect.ComposterEffectData;
import com.github.steveice10.mc.protocol.data.game.level.effect.DragonFireballEffectData;
import com.github.steveice10.mc.protocol.data.game.level.effect.ParticleEffect;
import com.github.steveice10.mc.protocol.data.game.level.effect.RecordEffectData;
import com.github.steveice10.mc.protocol.data.game.level.effect.SmokeEffectData;
import com.github.steveice10.mc.protocol.data.game.level.effect.SoundEffect;
import com.github.steveice10.mc.protocol.data.game.level.effect.WorldEffect;
import com.github.steveice10.mc.protocol.data.game.level.effect.WorldEffectData;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientboundLevelEventPacket implements Packet {
    private @NonNull WorldEffect effect;
    private @NonNull Position position;
    private @NonNull WorldEffectData data;
    private boolean broadcast;

    public ClientboundLevelEventPacket(@NonNull WorldEffect effect, @NonNull Position position, @NonNull WorldEffectData data) {
        this(effect, position, data, false);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.effect = MagicValues.key(WorldEffect.class, in.readInt());
        this.position = Position.read(in);
        int value = in.readInt();
        if (this.effect == SoundEffect.RECORD) {
            this.data = new RecordEffectData(value);
        } else if (this.effect == ParticleEffect.SMOKE) {
            this.data = MagicValues.key(SmokeEffectData.class, value % 6);
        } else if (this.effect == ParticleEffect.BREAK_BLOCK) {
            this.data = new BreakBlockEffectData(value);
        } else if (this.effect == ParticleEffect.BREAK_SPLASH_POTION) {
            this.data = new BreakPotionEffectData(value);
        } else if (this.effect == ParticleEffect.BONEMEAL_GROW || this.effect == ParticleEffect.BONEMEAL_GROW_WITH_SOUND) {
            this.data = new BonemealGrowEffectData(value);
        } else if (this.effect == ParticleEffect.COMPOSTER) {
            this.data = value > 0 ? ComposterEffectData.FILL_SUCCESS : ComposterEffectData.FILL;
        } else if (this.effect == ParticleEffect.ENDERDRAGON_FIREBALL_EXPLODE) {
            this.data = value == 1 ? DragonFireballEffectData.HAS_SOUND : DragonFireballEffectData.NO_SOUND;
        }

        this.broadcast = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(MagicValues.value(Integer.class, this.effect));
        Position.write(out, this.position);
        int value = 0;
        if (this.data instanceof RecordEffectData) {
            value = ((RecordEffectData) this.data).getRecordId();
        } else if (this.data instanceof SmokeEffectData) {
            value = MagicValues.value(Integer.class, (SmokeEffectData) this.data);
        } else if (this.data instanceof BreakBlockEffectData) {
            value = ((BreakBlockEffectData) this.data).getBlockState();
        } else if (this.data instanceof BreakPotionEffectData) {
            value = ((BreakPotionEffectData) this.data).getPotionId();
        } else if (this.data instanceof BonemealGrowEffectData) {
            value = ((BonemealGrowEffectData) this.data).getParticleCount();
        } else if (this.data instanceof ComposterEffectData) {
            value = MagicValues.value(Integer.class, this.data);
        } else if (this.data instanceof DragonFireballEffectData) {
            value = MagicValues.value(Integer.class, this.data);
        }

        out.writeInt(value);
        out.writeBoolean(this.broadcast);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
