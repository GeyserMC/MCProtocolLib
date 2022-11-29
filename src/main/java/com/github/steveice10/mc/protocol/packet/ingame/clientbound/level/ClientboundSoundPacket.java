package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.level.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.level.sound.CustomSound;
import com.github.steveice10.mc.protocol.data.game.level.sound.Sound;
import com.github.steveice10.mc.protocol.data.game.level.sound.SoundCategory;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSoundPacket implements MinecraftPacket {
    private final @NonNull Sound sound;
    private final float range;
    private final boolean isNewSystem;
    private final @NonNull SoundCategory category;
    private final double x;
    private final double y;
    private final double z;
    private final float volume;
    private final float pitch;
    private final long seed;

    public ClientboundSoundPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        int id = helper.readVarInt(in);
        if (id == 0) {
            this.sound = new CustomSound(helper.readString(in));
            this.isNewSystem = in.readBoolean();
            this.range = this.isNewSystem ? in.readFloat() : 16.0F;
        } else {
            this.sound = BuiltinSound.from(id - 1);
            this.isNewSystem = false;
            this.range = 16.0F;
        }
        this.category = helper.readSoundCategory(in);
        this.x = in.readInt() / 8D;
        this.y = in.readInt() / 8D;
        this.z = in.readInt() / 8D;
        this.volume = in.readFloat();
        this.pitch = in.readFloat();
        this.seed = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        if (this.sound instanceof CustomSound) {
            helper.writeVarInt(out, 0);
            helper.writeString(out, ((CustomSound) this.sound).getName());
            if (this.isNewSystem) {
                out.writeFloat(this.range);
            }
        } else {
            helper.writeVarInt(out, ((BuiltinSound)this.sound).ordinal() + 1);
        }
        helper.writeSoundCategory(out, this.category);
        out.writeInt((int) (this.x * 8));
        out.writeInt((int) (this.y * 8));
        out.writeInt((int) (this.z * 8));
        out.writeFloat(this.volume);
        out.writeFloat(this.pitch);
        out.writeLong(this.seed);
    }
}
