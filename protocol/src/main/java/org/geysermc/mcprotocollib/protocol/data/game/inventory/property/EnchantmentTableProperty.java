package org.geysermc.mcprotocollib.protocol.data.game.inventory.property;

/**
 * Container properties of an enchantment table.
 */
public enum EnchantmentTableProperty implements ContainerProperty {
    /**
     * Experience level required for the enchantment in slot 1, or 0 if the slot is empty.
     */
    LEVEL_SLOT_1,

    /**
     * Experience level required for the enchantment in slot 2, or 0 if the slot is empty.
     */
    LEVEL_SLOT_2,

    /**
     * Experience level required for the enchantment in slot 3, or 0 if the slot is empty.
     */
    LEVEL_SLOT_3,

    /**
     * The seed used for the next enchantment.
     */
    XP_SEED,

    /**
     * Enchantment registry ID shown as the hint for slot 1, or -1 if there is no hint.
     */
    ENCHANTMENT_SLOT_1,

    /**
     * Enchantment registry ID shown as the hint for slot 2, or -1 if there is no hint.
     */
    ENCHANTMENT_SLOT_2,

    /**
     * Enchantment registry ID shown as the hint for slot 3, or -1 if there is no hint.
     */
    ENCHANTMENT_SLOT_3,

    /**
     * Level of the hinted enchantment in slot 1, or -1 if there is no hint.
     */
    ENCHANTMENT_LEVEL_SLOT_1,

    /**
     * Level of the hinted enchantment in slot 2, or -1 if there is no hint.
     */
    ENCHANTMENT_LEVEL_SLOT_2,

    /**
     * Level of the hinted enchantment in slot 3, or -1 if there is no hint.
     */
    ENCHANTMENT_LEVEL_SLOT_3;

    private static final EnchantmentTableProperty[] VALUES = values();

    public static EnchantmentTableProperty from(int id) {
        return VALUES[id];
    }
}
