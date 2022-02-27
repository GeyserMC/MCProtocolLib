package com.github.steveice10.mc.protocol.data.game;

public final class Identifier {
    private Identifier() {
    }

    public static String formalize(String identifier) {
        return !identifier.contains(":") ? "minecraft:" + identifier : identifier;
    }
}
