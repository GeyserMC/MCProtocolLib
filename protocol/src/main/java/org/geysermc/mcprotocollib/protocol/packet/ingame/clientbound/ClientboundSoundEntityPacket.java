package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
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

    public ClientboundSoundEntityPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.sound = helper.readById(in, BuiltinSound::from, helper::readSoundEvent);
        this.category = helper.readSoundCategory(in);
        this.entityId = helper.readVarInt(in);
        this.volume = in.readFloat();
        this.pitch = in.readFloat();
        this.seed = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        if (this.sound instanceof CustomSound) {
            helper.writeVarInt(out, 0);
            helper.writeSoundEvent(out, this.sound);
        } else {
            helper.writeVarInt(out, ((BuiltinSound) this.sound).ordinal() + 1);
        }
        helper.writeSoundCategory(out, this.category);
        helper.writeVarInt(out, this.entityId);
        out.writeFloat(this.volume);
        out.writeFloat(this.pitch);
        out.writeLong(this.seed);
    }
}
