package com.github.steveice10.mc.protocol.packet.ingame.server;

import java.io.IOException;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.world.sound.SoundCategory;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ServerEntitySoundEffectPacket extends MinecraftPacket {

    private int soundId;
    private SoundCategory soundCategory;
    private int entityId;
    private float volume;
    private float pitch;

    public ServerEntitySoundEffectPacket() {
    }

    public ServerEntitySoundEffectPacket(int soundId, SoundCategory soundCategory, int entityId, float volume, float pitch) {
        this.soundId = soundId;
        this.soundCategory = soundCategory;
        this.entityId = entityId;
        this.volume = volume;
        this.pitch = pitch;
    }

    public int getSoundId() {
        return soundId;
    }

    public SoundCategory getSoundCategory() {
        return soundCategory;
    }

    public int getEntityId() {
        return entityId;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }

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
}
