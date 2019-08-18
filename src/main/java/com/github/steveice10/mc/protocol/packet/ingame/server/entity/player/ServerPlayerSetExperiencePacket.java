package com.github.steveice10.mc.protocol.packet.ingame.server.entity.player;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerPlayerSetExperiencePacket implements Packet {
    private float experience;
    private int level;
    private int totalExperience;

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
