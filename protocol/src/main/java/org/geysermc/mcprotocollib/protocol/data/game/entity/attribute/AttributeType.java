package org.geysermc.mcprotocollib.protocol.data.game.entity.attribute;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface AttributeType {

    String getIdentifier();

    int getId();

    // TODO: Reimplement once new format is finalized
//    /**
//     * Used when MCProtocolLib gets an attribute not in its built-in registry.
//     */
//    @Getter
//    @EqualsAndHashCode
//    class Custom implements AttributeType {
//        private final String identifier;
//
//        public Custom(String identifier) {
//            this.identifier = identifier;
//        }
//
//        public String getIdentifier() {
//            return identifier;
//        }
//    }

    @Getter
    @AllArgsConstructor
    enum Builtin implements AttributeType {
        GENERIC_ARMOR("minecraft:generic.armor", 0, 0, 30),
        GENERIC_ARMOR_TOUGHNESS("minecraft:generic.armor_toughness", 0, 0, 20),
        GENERIC_ATTACK_DAMAGE("minecraft:generic.attack_damage", 2, 0, 2048),
        GENERIC_ATTACK_KNOCKBACK("minecraft:generic.attack_knockback", 0, 0, 5),
        GENERIC_ATTACK_SPEED("minecraft:generic.attack_speed", 4, 0, 1024),
        PLAYER_BLOCK_BREAK_SPEED("minecraft:player.block_break_speed", 1, 0, 1024),
        PLAYER_BLOCK_INTERACTION_RANGE("minecraft:player.block_interaction_range", 4.5, 0, 64),
        GENERIC_BURNING_TIME("minecraft:generic.burning_time", 1, 0, 1024),
        GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE("minecraft:generic.explosion_knockback_resistance", 0, 0, 1),
        PLAYER_ENTITY_INTERACTION_RANGE("minecraft:player.entity_interaction_range", 3, 0, 64),
        GENERIC_FALL_DAMAGE_MULTIPLIER("minecraft:generic.fall_damage_multiplier", 1, 0, 100),
        GENERIC_FLYING_SPEED("minecraft:generic.flying_speed", 0.4, 0, 1024),
        GENERIC_FOLLOW_RANGE("minecraft:generic.follow_range", 32, 0, 2048),
        GENERIC_GRAVITY("minecraft:generic.gravity", 0.08, -1, 1),
        GENERIC_JUMP_STRENGTH("minecraft:generic.jump_strength", 0.42, 0, 32),
        GENERIC_KNOCKBACK_RESISTANCE("minecraft:generic.knockback_resistance", 0, 0, 1),
        GENERIC_LUCK("minecraft:generic.luck", 0, -1024, 1024),
        GENERIC_MAX_ABSORPTION("minecraft:generic.max_absorption", 0, 0, 2048),
        GENERIC_MAX_HEALTH("minecraft:generic.max_health", 20, 1, 1024),
        PLAYER_MINING_EFFICIENCY("player.mining_efficiency", 0, 0, 1024),
        GENERIC_MOVEMENT_EFFICIENCY("generic.movement_efficiency", 0, 0, 1),
        GENERIC_MOVEMENT_SPEED("minecraft:generic.movement_speed", 0.7, 0, 1024),
        GENERIC_OXYGEN_BONUS("minecraft:generic.oxygen_bonus", 0, 0, 1024),
        GENERIC_SAFE_FALL_DISTANCE("minecraft:generic.safe_fall_distance", 3, -1024, 1024),
        GENERIC_SCALE("minecraft:generic.scale", 1, 0.0625, 16),
        PLAYER_SNEAKING_SPEED("minecraft:player.sneaking_speed", 0.3, 0, 1),
        ZOMBIE_SPAWN_REINFORCEMENTS("minecraft:zombie.spawn_reinforcements", 0, 0, 1),
        GENERIC_STEP_HEIGHT("minecraft:generic.step_height", 0.6, 0, 10),
        PLAYER_SUBMERGED_MINING_SPEED("minecraft:player.submerged_mining_speed", 0.2, 0, 20),
        PLAYER_SWEEPING_DAMAGE_RATIO("minecraft:player.sweeping_damage_ratio", 0, 0, 1),
        GENERIC_WATER_MOVEMENT_EFFICIENCY("minecraft:generic.water_movement_efficiency", 0, 0, 1);

        private final String identifier;
        private final double def;
        private final double min;
        private final double max;

        public int getId() {
            return this.ordinal();
        }

        public static final Int2ObjectMap<AttributeType> BUILTIN = new Int2ObjectOpenHashMap<>();

        public static AttributeType from(int id) {
            return BUILTIN.get(id);
        }

        static {
            for (Builtin attribute : values()) {
                BUILTIN.put(attribute.ordinal(), attribute);
            }
        }
    }
}
