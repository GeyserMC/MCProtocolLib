package com.github.steveice10.mc.protocol.data.game.command;

public enum CommandParser {
    BOOL,
    FLOAT,
    DOUBLE,
    INTEGER,
    LONG,
    STRING,
    ENTITY,
    GAME_PROFILE,
    BLOCK_POS,
    COLUMN_POS,
    VEC3,
    VEC2,
    BLOCK_STATE,
    BLOCK_PREDICATE,
    ITEM_STACK,
    ITEM_PREDICATE,
    COLOR,
    COMPONENT,
    MESSAGE,
    NBT_COMPOUND_TAG,
    NBT_TAG,
    NBT_PATH,
    OBJECTIVE,
    OBJECTIVE_CRITERIA,
    OPERATION,
    PARTICLE,
    ANGLE,
    ROTATION,
    SCOREBOARD_SLOT,
    SCORE_HOLDER,
    SWIZZLE,
    TEAM,
    ITEM_SLOT,
    RESOURCE_LOCATION,
    FUNCTION,
    ENTITY_ANCHOR,
    INT_RANGE,
    FLOAT_RANGE,
    DIMENSION,
    GAMEMODE,
    TIME,
    RESOURCE_OR_TAG,
    RESOURCE_OR_TAG_KEY,
    RESOURCE,
    RESOURCE_KEY,
    TEMPLATE_MIRROR,
    TEMPLATE_ROTATION,
    HEIGHTMAP,
    UUID;

    private static final CommandParser[] VALUES = values();

    public static CommandParser from(int id) {
        return VALUES[id];
    }
}
