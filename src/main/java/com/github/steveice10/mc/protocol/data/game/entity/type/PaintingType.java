package com.github.steveice10.mc.protocol.data.game.entity.type;

public enum PaintingType {
    KEBAB,
    AZTEC,
    ALBAN,
    AZTEC2,
    BOMB,
    PLANT,
    WASTELAND,
    POOL,
    COURBET,
    SEA,
    SUNSET,
    CREEBET,
    WANDERER,
    GRAHAM,
    MATCH,
    BUST,
    STAGE,
    VOID,
    SKULL_AND_ROSES,
    WITHER,
    FIGHTERS,
    POINTER,
    PIGSCENE,
    BURNING_SKULL,
    SKELETON,
    EARTH,
    WIND,
    WATER,
    FIRE,
    DONKEY_KONG;

    private static final PaintingType[] VALUES = values();

    public static PaintingType from(int id) {
        return VALUES[id];
    }
}
