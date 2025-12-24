package org.geysermc.mcprotocollib.protocol.data.game.entity.type;

public interface PaintingType {

    int id();

    static PaintingType from(int id) {
        if (id < 0 || id >= BuiltinPaintingType.values().length) {
            return new Custom(id);
        }
        return BuiltinPaintingType.VALUES[id];
    }

    record Custom(int id) implements PaintingType {
    }
}
