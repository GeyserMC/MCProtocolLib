package com.github.steveice10.mc.protocol.data.game.level.notify;

public enum EnterCreditsValue implements GameEventValue {
    SEEN_BEFORE,
    FIRST_TIME;

    private static final EnterCreditsValue[] VALUES = values();

    public static EnterCreditsValue from(int id) {
        return VALUES[id];
    }
}
