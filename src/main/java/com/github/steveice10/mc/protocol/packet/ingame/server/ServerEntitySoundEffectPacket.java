package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.world.sound.SoundCategory;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerEntitySoundEffectPacket implements Packet {
    private int soundId;
    private @NonNull SoundCategory soundCategory;
    private int entityId;
    private float volume;
    private float pitch;

    @Override
    public void read(NetInput in) throws IOException {
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
