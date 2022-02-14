package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

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
import lombok.With;

import javax.annotation.Nullable;
import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundStopSoundPacket implements Packet {
    private static final int FLAG_CATEGORY = 0x01;
    private static final int FLAG_SOUND = 0x02;

    private final @Nullable SoundCategory category;
    private final @Nullable Sound sound;

    public ClientboundStopSoundPacket(NetInput in) throws IOException {
        int flags = in.readByte();
        if ((flags & FLAG_CATEGORY) != 0) {
            this.category = MagicValues.key(SoundCategory.class, in.readVarInt());
        } else {
            this.category = null;
        }

        if ((flags & FLAG_SOUND) != 0) {
            String value = in.readString();
            Sound sound = BuiltinSound.NAME_TO_SOUND.get(value);
            if (sound != null) {
                this.sound = sound;
            } else {
                this.sound = new CustomSound(value);
            }
        } else {
            this.sound = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        int flags = 0;
        if (this.category != null) {
            flags |= FLAG_CATEGORY;
        }

        if (this.sound != null) {
            flags |= FLAG_SOUND;
        }

        out.writeByte(flags);
        if (this.category != null) {
            out.writeByte(MagicValues.value(Integer.class, this.category));
        }

        if (this.sound != null) {
            String value = "";
            if (this.sound instanceof CustomSound) {
                value = ((CustomSound) this.sound).getName();
            } else if (this.sound instanceof BuiltinSound) {
                value = ((BuiltinSound) this.sound).getName();
            }

            out.writeString(value);
        }
    }
}
