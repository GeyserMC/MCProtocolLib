package com.github.steveice10.mc.protocol.data.game.level.map;

public enum MapIconType {
    WHITE_ARROW,
    GREEN_ARROW,
    RED_ARROW,
    BLUE_ARROW,
    WHITE_CROSS,
    RED_POINTER,
    WHITE_CIRCLE,
    SMALL_WHITE_CIRCLE,
    MANSION,
    TEMPLE,
    WHITE_BANNER,
    ORANGE_BANNER,
    MAGENTA_BANNER,
    LIGHT_BLUE_BANNER,
    YELLOW_BANNER,
    LIME_BANNER,
    PINK_BANNER,
    GRAY_BANNER,
    LIGHT_GRAY_BANNER,
    CYAN_BANNER,
    PURPLE_BANNER,
    BLUE_BANNER,
    BROWN_BANNER,
    GREEN_BANNER,
    RED_BANNER,
    BLACK_BANNER,
    TREASURE_MARKER,
    DESERT_VILLAGE,
    PLAINS_VILLAGE,
    SAVANNA_VILLAGE,
    SNOWY_VILLAGE,
    TAIGA_VILLAGE,
    JUNGLE_TEMPLE,
    SWAMP_HUT;

    private static final MapIconType[] VALUES = values();

    public static MapIconType from(int id) {
        return VALUES[id];
    }
}
