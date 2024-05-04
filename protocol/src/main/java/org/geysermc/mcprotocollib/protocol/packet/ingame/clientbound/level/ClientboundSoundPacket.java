package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
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

    public ClientboundSoundPacket(MinecraftByteBuf buf) {
        this.sound = buf.readById(BuiltinSound::from, buf::readSoundEvent);
        this.category = buf.readSoundCategory();
        this.x = buf.readInt() / 8D;
        this.y = buf.readInt() / 8D;
        this.z = buf.readInt() / 8D;
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
        this.seed = buf.readLong();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        if (this.sound instanceof CustomSound) {
            buf.writeVarInt(0);
            buf.writeSoundEvent(this.sound);
        } else {
            buf.writeVarInt(((BuiltinSound) this.sound).ordinal() + 1);
        }
        buf.writeSoundCategory(this.category);
        buf.writeInt((int) (this.x * 8));
        buf.writeInt((int) (this.y * 8));
        buf.writeInt((int) (this.z * 8));
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
        buf.writeLong(this.seed);
    }
}
