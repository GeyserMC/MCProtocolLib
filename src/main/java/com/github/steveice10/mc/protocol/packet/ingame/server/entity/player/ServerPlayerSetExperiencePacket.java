package com.github.steveice10.mc.protocol.packet.ingame.server.entity.player;

import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class ServerPlayerSetExperiencePacket implements Packet {

    private float experience;
    private int level;
    private int totalExperience;

    @SuppressWarnings("unused")
    private ServerPlayerSetExperiencePacket() {
    }

    public ServerPlayerSetExperiencePacket(float experience, int level, int totalExperience) {
        this.experience = experience;
        this.level = level;
        this.totalExperience = totalExperience;
    }

    public float getSlot() {
        return this.experience;
    }

    public int getLevel() {
        return this.level;
    }

    public int getTotalExperience() {
        return this.totalExperience;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.experience = in.readFloat();
        this.level = in.readVarInt();
        this.totalExperience = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeFloat(this.experience);
        out.writeVarInt(this.level);
        out.writeVarInt(this.totalExperience);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}
