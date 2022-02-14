package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.level.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.level.sound.CustomSound;
import com.github.steveice10.mc.protocol.data.game.level.sound.Sound;
import com.github.steveice10.mc.protocol.data.game.level.sound.SoundCategory;
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
public class ClientboundCustomSoundPacket implements Packet {
    private final @NonNull Sound sound;
    private final @NonNull SoundCategory category;
    private final double x;
    private final double y;
    private final double z;
    private final float volume;
    private final float pitch;

    public ClientboundCustomSoundPacket(NetInput in) throws IOException {
        String value = in.readString();
        Sound sound = BuiltinSound.NAME_TO_SOUND.get(value);
        if (sound != null) {
            this.sound = sound;
        } else {
            this.sound = new CustomSound(value);
        }

        this.category = MagicValues.key(SoundCategory.class, in.readVarInt());
        this.x = in.readInt() / 8D;
        this.y = in.readInt() / 8D;
        this.z = in.readInt() / 8D;
        this.volume = in.readFloat();
        this.pitch = in.readFloat();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        String value = "";
        if (this.sound instanceof CustomSound) {
            value = ((CustomSound) this.sound).getName();
        } else if (this.sound instanceof BuiltinSound) {
            value = ((BuiltinSound) this.sound).getName();
        }

        out.writeString(value);
        out.writeVarInt(MagicValues.value(Integer.class, this.category));
        out.writeInt((int) (this.x * 8));
        out.writeInt((int) (this.y * 8));
        out.writeInt((int) (this.z * 8));
        out.writeFloat(this.volume);
        out.writeFloat(this.pitch);
    }
}
