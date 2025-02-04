package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.BuiltinSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.CustomSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.SoundCategory;

@Data
@With
@AllArgsConstructor
public class ClientboundSoundPacket implements MinecraftPacket {
    private final @NonNull Sound sound;
    private final @NonNull SoundCategory category;
    private final double x;
    private final double y;
    private final double z;
    private final float volume;
    private final float pitch;
    private final long seed;

    public ClientboundSoundPacket(ByteBuf in) {
        this.sound = MinecraftTypes.readById(in, BuiltinSound::from, MinecraftTypes::readSoundEvent);
        this.category = MinecraftTypes.readSoundCategory(in);
        this.x = in.readInt() / 8D;
        this.y = in.readInt() / 8D;
        this.z = in.readInt() / 8D;
        this.volume = in.readFloat();
        this.pitch = in.readFloat();
        this.seed = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out) {
        if (this.sound instanceof CustomSound) {
            MinecraftTypes.writeVarInt(out, 0);
            MinecraftTypes.writeSoundEvent(out, this.sound);
        } else {
            MinecraftTypes.writeVarInt(out, ((BuiltinSound) this.sound).ordinal() + 1);
        }
        MinecraftTypes.writeSoundCategory(out, this.category);
        out.writeInt((int) (this.x * 8));
        out.writeInt((int) (this.y * 8));
        out.writeInt((int) (this.z * 8));
        out.writeFloat(this.volume);
        out.writeFloat(this.pitch);
        out.writeLong(this.seed);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
