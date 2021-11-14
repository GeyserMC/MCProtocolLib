package com.github.steveice10.mc.protocol.data.game.level.block;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;

import javax.annotation.Nonnull;
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

    private static final IntObjectMap<BlockEntityType> NETWORK_TO_BLOCK_ENTITY = new IntObjectHashMap<>();

    static {
        for (BlockEntityType type : values()) {
            NETWORK_TO_BLOCK_ENTITY.put(type.ordinal(), type);
        }
    }

    public static @Nonnull BlockEntityType read(NetInput in) throws IOException {
        int networkId = in.readVarInt();
        BlockEntityType type = NETWORK_TO_BLOCK_ENTITY.get(networkId);
        if (type == null) {
            throw new IllegalStateException("Cannot find type for network ID " + networkId);
        }
        return type;
    }

    public static void write(NetOutput out, @Nonnull BlockEntityType type) throws IOException {
        out.writeVarInt(type.ordinal());
    }
}
