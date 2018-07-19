package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.CustomSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.Sound;
import com.github.steveice10.mc.protocol.data.game.world.sound.SoundCategory;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ServerStopSoundPacket extends MinecraftPacket {
    private SoundCategory category;
    private Sound sound;

    @SuppressWarnings("unused")
    private ServerStopSoundPacket() {
    }

    public ServerStopSoundPacket(SoundCategory category, Sound sound) {
        this.category = category;
        this.sound = sound;
    }

    public SoundCategory getCategory() {
        return this.category;
    }

    public Sound getSound() {
        return sound;
    }

    @Override
    public void read(NetInput in) throws IOException {
        int flags = in.readByte();
        if((flags & 0x1) != 0) {
            this.category = MagicValues.key(SoundCategory.class, in.readVarInt());
        } else {
            this.category = null;
        }
        if((flags & 0x2) != 0) {
            String value = in.readString();
            try {
                this.sound = MagicValues.key(BuiltinSound.class, value);
            } catch(IllegalArgumentException e) {
                this.sound = new CustomSound(value);
            }
        } else {
            this.sound = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte((this.category != null ? 0x1 : 0) | (this.sound != null ? 0x2 : 0));
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
}
