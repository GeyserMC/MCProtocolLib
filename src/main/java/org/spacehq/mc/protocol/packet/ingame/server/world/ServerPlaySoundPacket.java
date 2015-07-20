package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.world.CustomSound;
import org.spacehq.mc.protocol.data.game.values.world.GenericSound;
import org.spacehq.mc.protocol.data.game.values.world.Sound;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerPlaySoundPacket implements Packet {

    private Sound sound;
    private double x;
    private double y;
    private double z;
    private float volume;
    private float pitch;

    @SuppressWarnings("unused")
    private ServerPlaySoundPacket() {
    }

    public ServerPlaySoundPacket(Sound sound, double x, double y, double z, float volume, float pitch) {
        this.sound = sound;
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = volume;
        this.pitch = pitch;
    }

    public Sound getSound() {
        return this.sound;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    @Override
    public void read(NetInput in) throws IOException {
        String value = in.readString();
        this.sound = MagicValues.key(GenericSound.class, value);
        if(this.sound == null) {
            this.sound = new CustomSound(value);
        }

        this.x = in.readInt() / 8D;
        this.y = in.readInt() / 8D;
        this.z = in.readInt() / 8D;
        this.volume = in.readFloat();
        this.pitch = in.readUnsignedByte() / 63f;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        String value = "";
        if(this.sound instanceof CustomSound) {
            value = ((CustomSound) this.sound).getName();
        } else if(this.sound instanceof GenericSound) {
            value = MagicValues.value(String.class, (GenericSound) this.sound);
        }

        out.writeString(value);
        out.writeInt((int) (this.x * 8));
        out.writeInt((int) (this.y * 8));
        out.writeInt((int) (this.z * 8));
        out.writeFloat(this.volume);
        int pitch = (int) (this.pitch * 63);
        if(pitch > 255) {
            pitch = 255;
        }

        if(pitch < 0) {
            pitch = 0;
        }

        out.writeByte(pitch);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
