package org.geysermc.mcprotocollib.protocol.data.game.level.block;

import org.checkerframework.checker.nullness.qual.Nullable;

public enum BlockEntityType {
    FURNACE,
    CHEST,
    TRAPPED_CHEST,
    ENDER_CHEST,
    JUKEBOX,
    DISPENSER,
    DROPPER,
    SIGN,
    HANGING_SIGN,
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
    SCULK_SENSOR,
    CALIBRATED_SCULK_SENSOR,
    SCULK_CATALYST,
    SCULK_SHRIEKER,
    CHISELED_BOOKSHELF,
    BRUSHABLE_BLOCK,
    DECORATED_POT,
    CRAFTER,
    TRIAL_SPAWNER,
    VAULT;

    private static final BlockEntityType[] VALUES = values();

    @Nullable
    public static BlockEntityType from(int id) {
        if (id >= 0 && id < VALUES.length) {
            return VALUES[id];
        } else {
            return null;
        }
    }
}
