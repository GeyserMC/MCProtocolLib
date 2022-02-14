package com.github.steveice10.mc.protocol.data.game.level.block;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public enum BlockEntityType {
    FURNACE,
    CHEST,
    TRAPPED_CHEST,
    ENDER_CHEST,
    JUKEBOX,
    DISPENSER,
    DROPPER,
    SIGN,
    MOB_SPAWNER,
    PISTON,
    BREWING_STAND,
    ENCHANTING_TABLE,
    END_PORTAL,
    BEACON,
    SKULL,
    DAYLIGHT_DETECTOR,
    HOPPER,
    COMPARATOR,
    BANNER,
    STRUCTURE_BLOCK,
    END_GATEWAY,
    COMMAND_BLOCK,
    SHULKER_BOX,
    BED,
    CONDUIT,
    BARREL,
    SMOKER,
    BLAST_FURNACE,
    LECTERN,
    BELL,
    JIGSAW,
    CAMPFIRE,
    BEEHIVE,
    SCULK_SENSOR;

    private static final BlockEntityType[] VALUES = values();

    public static BlockEntityType read(NetInput in) throws IOException {
        int id = in.readVarInt();
        if (id >= 0 && id < VALUES.length) {
            return VALUES[id];
        } else {
            return null;
        }
    }

    public static void write(NetOutput out, BlockEntityType type) throws IOException {
        if (type == null) {
            out.writeVarInt(-1);
        } else {
            out.writeVarInt(type.ordinal());
        }
    }
}
