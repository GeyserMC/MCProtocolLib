package com.github.steveice10.mc.protocol.data.game.inventory.property;

/**
 * Container properties of an enchantment table.
 */
public enum EnchantmentTableProperty implements ContainerProperty {
    /**
     * Level of the enchantment in slot 1.
     */
    LEVEL_SLOT_1,

    /**
     * Level of the enchantment in slot 2.
     */
    LEVEL_SLOT_2,

    /**
     * Level of the enchantment in slot 3.
     */
    LEVEL_SLOT_3,

    /**
     * The seed used for the next enchantment.
     */
    XP_SEED,

    /**
     * The enchantment for slot 1.
     *
     * @see #getEnchantment(int, int)
     */
    ENCHANTMENT_SLOT_1,

    /**
     * The enchantment for slot 2.
     *
     * @see #getEnchantment(int, int)
     */
    ENCHANTMENT_SLOT_2,

    /**
     * The enchantment for slot 3.
     *
     * @see #getEnchantment(int, int)
     */
    ENCHANTMENT_SLOT_3;

    private static final EnchantmentTableProperty[] VALUES = values();

    public static EnchantmentTableProperty from(int id) {
        return VALUES[id];
    }

    /**
     * Packs enchantment type and level into one integer as used for the ENCHANTMENT_SLOT_X properties.
     *
     * @param type  Id of the enchantment
     * @param level Level of the enchantment
     * @return Packed value
     * @see #getEnchantmentType(int)
     * @see #getEnchantmentLevel(int)
     */
    public static int getEnchantment(int type, int level) {
        return type | level << 8;
    }

    /**
     * Unpacks the enchantment type from one integer as used for the ENCHANTMENT_SLOT_X properties.
     *
     * @param enchantmentInfo Packed value
     * @return Id of the enchantment
     * @see #getEnchantment(int, int)
     */
    public static int getEnchantmentType(int enchantmentInfo) {
        return enchantmentInfo & 0xff;
    }

    /**
     * Unpacks the enchantment level from one integer as used for the ENCHANTMENT_SLOT_X properties.
     *
     * @param enchantmentInfo Packed value
     * @return Level of the enchantment
     * @see #getEnchantment(int, int)
     */
    public static int getEnchantmentLevel(int enchantmentInfo) {
        return enchantmentInfo >> 8;
    }
}
