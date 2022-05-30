package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.game.level.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.level.sound.SoundCategory;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSoundEntityPacket implements Packet {
    private final @NonNull BuiltinSound sound;
    private final @NonNull SoundCategory category;
    private final int entityId;
    private final float volume;
    private final float pitch;
    private final long seed;

    public ClientboundSoundEntityPacket(NetInput in) throws IOException {
        this.sound = BuiltinSound.VALUES[in.readVarInt()];
        this.category = SoundCategory.read(in);
        this.entityId = in.readVarInt();
        this.volume = in.readFloat();
        this.pitch = in.readFloat();
        this.seed = in.readLong();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.sound.ordinal());
        out.writeVarInt(this.category.ordinal());
        out.writeVarInt(this.entityId);
        out.writeFloat(this.volume);
        out.writeFloat(this.pitch);
        out.writeLong(this.seed);
    }
}
