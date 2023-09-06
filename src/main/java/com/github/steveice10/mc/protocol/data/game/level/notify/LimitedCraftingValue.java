package com.github.steveice10.mc.protocol.data.game.level.notify;

public enum LimitedCraftingValue implements GameEventValue {
    UNLIMITED_CRAFTING,
    LIMITED_CRAFTING;

    private static final LimitedCraftingValue[] VALUES = values();

    public static LimitedCraftingValue from(int id) {
        return VALUES[id];
    }
}
