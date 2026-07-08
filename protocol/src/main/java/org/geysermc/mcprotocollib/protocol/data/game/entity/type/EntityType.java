package org.geysermc.mcprotocollib.protocol.data.game.entity.type;

public interface EntityType {

    int id();

    boolean isProjectile();

    static EntityType from(int id) {
        if (id < 0 || id >= BuiltinEntityType.VALUES.length) {
            return new Custom(id);
        }
        return BuiltinEntityType.VALUES[id];
    }

    default boolean is(EntityType type) {
        return id() == type.id();
    }

    record Custom(int id) implements EntityType {
        @Override
        public boolean isProjectile() {
            return false;
        }
    }
}
