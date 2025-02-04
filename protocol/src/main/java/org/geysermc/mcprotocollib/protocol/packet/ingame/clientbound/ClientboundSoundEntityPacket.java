package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

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
public class ClientboundSoundEntityPacket implements MinecraftPacket {
    private final @NonNull Sound sound;
    private final @NonNull SoundCategory category;
    private final int entityId;
    private final float volume;
    private final float pitch;
    private final long seed;

    public ClientboundSoundEntityPacket(ByteBuf in) {
        this.sound = MinecraftTypes.readById(in, BuiltinSound::from, MinecraftTypes::readSoundEvent);
        this.category = MinecraftTypes.readSoundCategory(in);
        this.entityId = MinecraftTypes.readVarInt(in);
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
        MinecraftTypes.writeVarInt(out, this.entityId);
        out.writeFloat(this.volume);
        out.writeFloat(this.pitch);
        out.writeLong(this.seed);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
