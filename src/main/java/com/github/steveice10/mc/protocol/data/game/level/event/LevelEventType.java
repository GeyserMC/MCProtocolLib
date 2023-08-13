package com.github.steveice10.mc.protocol.data.game.level.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LevelEventType implements LevelEvent {
    BLOCK_DISPENSER_DISPENSE(1000),
    BLOCK_DISPENSER_FAIL(1001),
    BLOCK_DISPENSER_LAUNCH(1002),
    ENTITY_ENDEREYE_LAUNCH(1003),
    ENTITY_FIREWORK_SHOOT(1004),
    BLOCK_FIRE_EXTINGUISH(1009),
    RECORD(1010),
    STOP_RECORD(1011), // As of 1.19.4
    ENTITY_GHAST_WARN(1015),
    ENTITY_GHAST_SHOOT(1016),
    ENTITY_ENDERDRAGON_SHOOT(1017),
    ENTITY_BLAZE_SHOOT(1018),
    ENTITY_ZOMBIE_ATTACK_DOOR_WOOD(1019),
    ENTITY_ZOMBIE_ATTACK_DOOR_IRON(1020),
    ENTITY_ZOMBIE_BREAK_DOOR_WOOD(1021),
    ENTITY_WITHER_BREAK_BLOCK(1022),
    ENTITY_WITHER_SPAWN(1023), // Global level event
    ENTITY_WITHER_SHOOT(1024),
    ENTITY_BAT_TAKEOFF(1025),
    ENTITY_ZOMBIE_INFECT(1026),
    ENTITY_ZOMBIE_VILLAGER_CONVERTED(1027),
    ENTITY_ENDERDRAGON_DEATH(1028), // Global level event
    BLOCK_ANVIL_DESTROY(1029),
    BLOCK_ANVIL_USE(1030),
    BLOCK_ANVIL_LAND(1031),
    BLOCK_PORTAL_TRAVEL(1032),
    BLOCK_CHORUS_FLOWER_GROW(1033),
    BLOCK_CHORUS_FLOWER_DEATH(1034),
    BLOCK_BREWING_STAND_BREW(1035),
    BLOCK_END_PORTAL_SPAWN(1038), // Global level event
    ENTITY_PHANTOM_BITE(1039),
    ENTITY_ZOMBIE_CONVERTED_TO_DROWNED(1040),
    ENTITY_HUSK_CONVERTED_TO_ZOMBIE(1041),
    BLOCK_GRINDSTONE_USE(1042),
    ITEM_BOOK_PAGE_TURN(1043),
    BLOCK_SMITHING_TABLE_USE(1044),
    POINTED_DRIPSTONE_LAND(1045),
    DRIP_LAVA_INTO_CAULDRON(1046),
    DRIP_WATER_INTO_CAULDRON(1047),
    ENTITY_SKELETON_CONVERTED_TO_STRAY(1048),

    COMPOSTER(1500),
    BLOCK_LAVA_EXTINGUISH(1501),
    BLOCK_REDSTONE_TORCH_BURNOUT(1502),
    BLOCK_END_PORTAL_FRAME_FILL(1503),
    DRIPSTONE_DRIP(1504),
    BONEMEAL_GROW_WITH_SOUND(1505),

    SMOKE(2000),
    BREAK_BLOCK(2001),
    BREAK_SPLASH_POTION(2002),
    BREAK_EYE_OF_ENDER(2003),
    MOB_SPAWN(2004),
    BONEMEAL_GROW(2005),
    ENDERDRAGON_FIREBALL_EXPLODE(2006),
    BREAK_SPLASH_POTION2(2007), // Mojank
    EXPLOSION(2008),
    EVAPORATE(2009),

    END_GATEWAY_SPAWN(3000),
    ENTITY_ENDERDRAGON_GROWL(3001),
    ELECTRIC_SPARK(3002),
    WAX_ON(3003),
    WAX_OFF(3004),
    SCRAPE(3005),
    SCULK_BLOCK_CHARGE(3006),
    SCULK_SHRIEKER_SHRIEK(3007),
    BRUSH_BLOCK_COMPLETE(3008),
    EGG_CRACK(3009);

    private final int id;

    @Override
    public int getId() {
        return id;
    }
}
