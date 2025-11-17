package org.geysermc.mcprotocollib.protocol.data.game.entity.type;

public enum BuiltinPaintingType implements PaintingType {
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
    DONKEY_KONG,
    BAROQUE,
    HUMBLE,
    MEDITATIVE,
    PRAIRIE_RIDE,
    UNPACKED,
    BACKYARD,
    BOUQUET,
    CAVEBIRD,
    CHANGING,
    COTAN,
    ENDBOSS,
    FERN,
    FINDING,
    LOWMIST,
    ORB,
    OWLEMONS,
    PASSAGE,
    POND,
    SUNFLOWERS,
    TIDES;

    public static final BuiltinPaintingType[] VALUES = values();

    @Override
    public int id() {
        return ordinal();
    }
}
