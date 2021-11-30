package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.MagicValues;
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
    private final int soundId;
    private final @NonNull SoundCategory soundCategory;
    private final int entityId;
    private final float volume;
    private final float pitch;

    public ClientboundSoundEntityPacket(NetInput in) throws IOException {
        this.soundId = in.readVarInt();
        this.soundCategory = MagicValues.key(SoundCategory.class, in.readVarInt());
        this.entityId = in.readVarInt();
        this.volume = in.readFloat();
        this.pitch = in.readFloat();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.soundId);
        out.writeVarInt(MagicValues.value(Integer.class, this.soundCategory));
        out.writeVarInt(this.entityId);
        out.writeFloat(this.volume);
        out.writeFloat(this.pitch);
    }
}
