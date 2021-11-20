package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@Data
@AllArgsConstructor
public class VillagerData {
    private final int type;
    private final int profession;
    private final int level;

    public static VillagerData read(NetInput in) throws IOException {
        return new VillagerData(in.readVarInt(), in.readVarInt(), in.readVarInt());
    }

    public static void write(NetOutput out, VillagerData villagerData) throws IOException {
        out.writeVarInt(villagerData.getType());
        out.writeVarInt(villagerData.getProfession());
        out.writeVarInt(villagerData.getLevel());
    }
}
