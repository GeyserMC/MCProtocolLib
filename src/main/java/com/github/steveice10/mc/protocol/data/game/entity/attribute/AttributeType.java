package com.github.steveice10.mc.protocol.data.game.entity.attribute;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public interface AttributeType {

    String getIdentifier();

    /**
     * Used when MCProtocolLib gets an attribute not in its built-in registry.
     */
    @Getter
    @EqualsAndHashCode
    class Custom implements AttributeType {
        private final String identifier;

        public Custom(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }
    }

    @Getter
    @AllArgsConstructor
    enum Builtin implements AttributeType {
        GENERIC_MAX_HEALTH("minecraft:generic.max_health", 20, 0, 1024),
        GENERIC_FOLLOW_RANGE("minecraft:generic.follow_range", 32, 0, 2048),
        GENERIC_KNOCKBACK_RESISTANCE("minecraft:generic.knockback_resistance", 0, 0, 1),
        GENERIC_MOVEMENT_SPEED("minecraft:generic.movement_speed", 0.699999988079071, 0, 1024),
        GENERIC_ATTACK_DAMAGE("minecraft:generic.attack_damage", 2, 0, 2048),
        GENERIC_ATTACK_SPEED("minecraft:generic.attack_speed", 4, 0, 1024),
        GENERIC_FLYING_SPEED("minecraft:generic.flying_speed", 0.4000000059604645, 0, 1024),
        GENERIC_ARMOR("minecraft:generic.armor", 0, 0, 30),
        GENERIC_ARMOR_TOUGHNESS("minecraft:generic.armor_toughness", 0, 0, 20),
        GENERIC_ATTACK_KNOCKBACK("minecraft:generic.attack_knockback", 0, 0, 5),
        GENERIC_LUCK("minecraft:generic.luck", 0, -1024, 1024),
        HORSE_JUMP_STRENGTH("minecraft:horse.jump_strength", 0.7, 0, 2),
        ZOMBIE_SPAWN_REINFORCEMENTS("minecraft:zombie.spawn_reinforcements", 0, 0, 1);

        private final String identifier;
        private final double def;
        private final double min;
        private final double max;

        public static final Map<String, AttributeType> BUILTIN = new HashMap<>();

        static {
            register(GENERIC_MAX_HEALTH);
            register(GENERIC_FOLLOW_RANGE);
            register(GENERIC_KNOCKBACK_RESISTANCE);
            register(GENERIC_MOVEMENT_SPEED);
            register(GENERIC_ATTACK_DAMAGE);
            register(GENERIC_ATTACK_SPEED);
            register(GENERIC_FLYING_SPEED);
            register(GENERIC_ARMOR);
            register(GENERIC_ARMOR_TOUGHNESS);
            register(GENERIC_ATTACK_KNOCKBACK);
            register(GENERIC_LUCK);
            register(HORSE_JUMP_STRENGTH);
            register(ZOMBIE_SPAWN_REINFORCEMENTS);
        }

        private static void register(AttributeType attributeType) {
            BUILTIN.put(attributeType.getIdentifier(), attributeType);
        }
    }
}
