package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.SoundCategory;

@Data
@With
@AllArgsConstructor
public class ClientboundStopSoundPacket implements MinecraftPacket {
    private static final int FLAG_CATEGORY = 0x01;
    private static final int FLAG_SOUND = 0x02;

    private final @Nullable SoundCategory category;
    private final @Nullable String sound;

    public ClientboundStopSoundPacket(ByteBuf in, MinecraftCodecHelper helper) {
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
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
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
