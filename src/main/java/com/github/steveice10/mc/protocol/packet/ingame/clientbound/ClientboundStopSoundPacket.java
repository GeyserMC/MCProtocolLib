package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.level.sound.SoundCategory;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundStopSoundPacket implements MinecraftPacket {
    private static final int FLAG_CATEGORY = 0x01;
    private static final int FLAG_SOUND = 0x02;

    private final @Nullable SoundCategory category;
    private final @Nullable String sound;

    public ClientboundStopSoundPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        int flags = in.readByte();
        if ((flags & FLAG_CATEGORY) != 0) {
            this.category = helper.readSoundCategory(in);
        } else {
            this.category = null;
        }

        if ((flags & FLAG_SOUND) != 0) {
            this.sound = helper.readResourceLocation(in);
        } else {
            this.sound = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        int flags = 0;
        if (this.category != null) {
            flags |= FLAG_CATEGORY;
        }

        if (this.sound != null) {
            flags |= FLAG_SOUND;
        }

        out.writeByte(flags);
        if (this.category != null) {
            out.writeByte(this.category.ordinal());
        }

        if (this.sound != null) {
            helper.writeResourceLocation(out, this.sound);
        }
    }
}
