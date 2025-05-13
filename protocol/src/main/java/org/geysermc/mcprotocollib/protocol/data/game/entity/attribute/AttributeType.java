package org.geysermc.mcprotocollib.protocol.data.game.entity.attribute;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;

public interface AttributeType {

    Key getIdentifier();

    int getId();

    // TODO: Reimplement once new format is finalized
//    /**
//     * Used when MCProtocolLib gets an attribute not in its built-in registry.
//     */
//    @Getter
//    @EqualsAndHashCode
//    class Custom implements AttributeType {
//        private final Key identifier;
//
//        public Custom(Key identifier) {
//            this.identifier = identifier;
//        }
//
//        public Key getIdentifier() {
//            return identifier;
//        }
//    }

    @Getter
    enum Builtin implements AttributeType {
        ARMOR("minecraft:armor", 0, 0, 30),
        ARMOR_TOUGHNESS("minecraft:armor_toughness", 0, 0, 20),
        ATTACK_DAMAGE("minecraft:attack_damage", 2, 0, 2048),
        ATTACK_KNOCKBACK("minecraft:attack_knockback", 0, 0, 5),
        ATTACK_SPEED("minecraft:attack_speed", 4, 0, 1024),
        BLOCK_BREAK_SPEED("minecraft:block_break_speed", 1, 0, 1024),
        BLOCK_INTERACTION_RANGE("minecraft:block_interaction_range", 4.5, 0, 64),
        BURNING_TIME("minecraft:burning_time", 1, 0, 1024),
        CAMERA_DISTANCE("minecraft:camera_distance", 4, 0, 32),
        EXPLOSION_KNOCKBACK_RESISTANCE("minecraft:explosion_knockback_resistance", 0, 0, 1),
        ENTITY_INTERACTION_RANGE("minecraft:entity_interaction_range", 3, 0, 64),
        FALL_DAMAGE_MULTIPLIER("minecraft:fall_damage_multiplier", 1, 0, 100),
        FLYING_SPEED("minecraft:flying_speed", 0.4, 0, 1024),
        FOLLOW_RANGE("minecraft:follow_range", 32, 0, 2048),
        GRAVITY("minecraft:gravity", 0.08, -1, 1),
        JUMP_STRENGTH("minecraft:jump_strength", 0.42, 0, 32),
        KNOCKBACK_RESISTANCE("minecraft:knockback_resistance", 0, 0, 1),
        LUCK("minecraft:luck", 0, -1024, 1024),
        MAX_ABSORPTION("minecraft:max_absorption", 0, 0, 2048),
        MAX_HEALTH("minecraft:max_health", 20, 1, 1024),
        MINING_EFFICIENCY("minecraft:mining_efficiency", 0, 0, 1024),
        MOVEMENT_EFFICIENCY("minecraft:movement_efficiency", 0, 0, 1),
        MOVEMENT_SPEED("minecraft:movement_speed", 0.7, 0, 1024),
        OXYGEN_BONUS("minecraft:oxygen_bonus", 0, 0, 1024),
        SAFE_FALL_DISTANCE("minecraft:safe_fall_distance", 3, -1024, 1024),
        SCALE("minecraft:scale", 1, 0.0625, 16),
        SNEAKING_SPEED("minecraft:sneaking_speed", 0.3, 0, 1),
        SPAWN_REINFORCEMENTS("minecraft:spawn_reinforcements", 0, 0, 1),
        STEP_HEIGHT("minecraft:step_height", 0.6, 0, 10),
        SUBMERGED_MINING_SPEED("minecraft:submerged_mining_speed", 0.2, 0, 20),
        SWEEPING_DAMAGE_RATIO("minecraft:sweeping_damage_ratio", 0, 0, 1),
        TEMPT_RANGE("minecraft:tempt_range", 10, 0, 2048),
        WATER_MOVEMENT_EFFICIENCY("minecraft:water_movement_efficiency", 0, 0, 1),
        WAYPOINT_TRANSMIT_RANGE("minecraft:waypoint_transmit_range", 0, 0, 6.0E7),
        WAYPOINT_RECEIVE_RANGE("minecraft:waypoint_receive_range", 0, 0, 6.0E7);

        private final Key identifier;
        private final double def;
        private final double min;
        private final double max;

        Builtin(@KeyPattern String identifier, double def, double min, double max) {
            this.identifier = Key.key(identifier);
            this.def = def;
            this.min = min;
            this.max = max;
        }

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
