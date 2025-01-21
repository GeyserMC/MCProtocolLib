package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.SoundCategory;

@Data
@With
@AllArgsConstructor
public class ClientboundStopSoundPacket implements MinecraftPacket {
    private static final int FLAG_CATEGORY = 0x01;
    private static final int FLAG_SOUND = 0x02;

    private final @Nullable SoundCategory category;
    private final @Nullable Key sound;

    public ClientboundStopSoundPacket(ByteBuf in) {
        int flags = in.readByte();
        if ((flags & FLAG_CATEGORY) != 0) {
            this.category = MinecraftTypes.readSoundCategory(in);
        } else {
            this.category = null;
        }

        if ((flags & FLAG_SOUND) != 0) {
            this.sound = MinecraftTypes.readResourceLocation(in);
        } else {
            this.sound = null;
        }
    }

    @Override
    public void serialize(ByteBuf out) {
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
            MinecraftTypes.writeResourceLocation(out, this.sound);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
