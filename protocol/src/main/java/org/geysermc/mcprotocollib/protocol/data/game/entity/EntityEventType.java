package org.geysermc.mcprotocollib.protocol.data.game.entity;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;

@AllArgsConstructor
public enum EntityEventType implements EntityEvent {
    TIPPED_ARROW_EMIT_PARTICLES(0),
    RABBIT_JUMP_OR_MINECART_SPAWNER_DELAY_RESET(1),
    KINETIC_HIT(2),
    LIVING_DEATH(3),
    ATTACK(4),
    STOP_ATTACK(5), // Unused
    TAMEABLE_TAMING_FAILED(6),
    TAMEABLE_TAMING_SUCCEEDED(7),
    WOLF_SHAKE_WATER(8),
    PLAYER_FINISH_USING_ITEM(9),
    SHEEP_GRAZE_OR_TNT_CART_EXPLODE(10),
    IRON_GOLEM_HOLD_POPPY(11),
    VILLAGER_MATE(12),
    VILLAGER_ANGRY(13),
    VILLAGER_HAPPY(14),
    WITCH_EMIT_PARTICLES(15),
    ZOMBIE_VILLAGER_CURE(16),
    FIREWORK_EXPLODE(17),
    ANIMAL_EMIT_HEARTS(18),
    SQUID_RESET_ROTATION(19),
    MOB_EMIT_SMOKE(20),
    GUARDIAN_MAKE_SOUND(21),
    PLAYER_ENABLE_REDUCED_DEBUG(22),
    PLAYER_DISABLE_REDUCED_DEBUG(23),
    PLAYER_SET_NO_PERMISSIONS(24),
    PLAYER_SET_MODERATOR(25),
    PLAYER_SET_GAMEMASTER(26),
    PLAYER_SET_ADMIN(27),
    PLAYER_SET_OWNER(28),
    FISHING_HOOK_PULL_PLAYER(31),
    ARMOR_STAND_HIT(32),
    IRON_GOLEM_EMPTY_HAND(34),
    TOTEM_OF_UNDYING_MAKE_SOUND(35),
    DOLPHIN_HAPPY(38),
    RAVAGER_STUNNED(39),
    OCELOT_TAMING_FAILED(40),
    OCELOT_TAMING_SUCCEEDED(41),
    VILLAGER_SWEAT(42), // water particle emitted randomly while raid is active
    FOX_EATING(45),
    LIVING_TELEPORT(46),
    LIVING_EQUIPMENT_BREAK_MAIN_HAND(47),
    LIVING_EQUIPMENT_BREAK_OFF_HAND(48),
    LIVING_EQUIPMENT_BREAK_HEAD(49),
    LIVING_EQUIPMENT_BREAK_CHEST(50),
    LIVING_EQUIPMENT_BREAK_LEGS(51),
    LIVING_EQUIPMENT_BREAK_FEET(52),
    HONEY_BLOCK_SLIDE(53),
    HONEY_BLOCK_LAND(54),
    PLAYER_SWAP_SAME_ITEM(55),
    WOLF_SHAKE_WATER_STOP(56),
    GOAT_LOWERING_HEAD(58),
    GOAT_STOP_LOWERING_HEAD(59),
    MAKE_POOF_PARTICLES(60),
    WARDEN_RECEIVE_SIGNAL(61),
    WARDEN_SONIC_BOOM(62),
    SNIFFER_MAKE_SOUND(63),
    ARMADILLO_PEEKING(64),
    LIVING_EQUIPMENT_BREAK_BODY(65),
    SHAKE(66),
    DROWN_PARTICLES(67),
    SADDLE_BREAK(68),
    RAVAGER_ROARED(69),
    TNT_PRIME(70),
    BOAT_SINK(71),
    BOAT_LAUNCH(72);

    private static final Int2ObjectMap<EntityEventType> VALUES;

    static {
        EntityEventType[] values = values();
        VALUES = new Int2ObjectOpenHashMap<>(values.length);
        for (EntityEventType value : values) {
            VALUES.put(value.getId(), value);
        }
    }

    private final int id;

    public static @Nullable EntityEventType from(int id) {
        return VALUES.get(id);
    }

    @Override
    public int getId() {
        return id;
    }
}
