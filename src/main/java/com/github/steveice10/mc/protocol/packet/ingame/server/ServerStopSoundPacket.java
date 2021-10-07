package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.UnmappedValueException;
import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.CustomSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.Sound;
import com.github.steveice10.mc.protocol.data.game.world.sound.SoundCategory;
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
public class ServerStopSoundPacket implements Packet {
    private static final int FLAG_CATEGORY = 0x01;
    private static final int FLAG_SOUND = 0x02;

    private @NonNull SoundCategory category;
    private @NonNull Sound sound;

    @Override
    public void read(NetInput in) throws IOException {
        int flags = in.readByte();
        if ((flags & FLAG_CATEGORY) != 0) {
            this.category = MagicValues.key(SoundCategory.class, in.readVarInt());
        } else {
            this.category = null;
        }

        if ((flags & FLAG_SOUND) != 0) {
            String value = in.readString();
            try {
                this.sound = MagicValues.key(BuiltinSound.class, value);
            } catch (UnmappedValueException e) {
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
                value = MagicValues.value(String.class, this.sound);
            }

            out.writeString(value);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
