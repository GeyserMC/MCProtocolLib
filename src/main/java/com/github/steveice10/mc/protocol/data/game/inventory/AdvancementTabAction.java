package com.github.steveice10.mc.protocol.data.game.inventory;

public enum AdvancementTabAction {
    OPENED_TAB,
    CLOSED_SCREEN;

    private static final AdvancementTabAction[] VALUES = values();

    public static AdvancementTabAction from(int id) {
        return VALUES[id];
    }
}
