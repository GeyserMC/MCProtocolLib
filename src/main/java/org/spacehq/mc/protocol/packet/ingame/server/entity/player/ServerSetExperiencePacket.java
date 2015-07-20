package org.spacehq.mc.protocol.packet.ingame.server.entity.player;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerSetExperiencePacket implements Packet {

    private float experience;
    private int level;
    private int totalExperience;

    @SuppressWarnings("unused")
    private ServerSetExperiencePacket() {
    }

    public ServerSetExperiencePacket(float experience, int level, int totalExperience) {
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

}
