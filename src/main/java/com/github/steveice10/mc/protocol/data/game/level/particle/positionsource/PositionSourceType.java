package com.github.steveice10.mc.protocol.data.game.level.particle.positionsource;

import com.github.steveice10.mc.protocol.data.game.Identifier;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum PositionSourceType {
    BLOCK,
    ENTITY;

    private final String resourceLocation;

    PositionSourceType() {
        this.resourceLocation = Identifier.formalize(name().toLowerCase(Locale.ROOT));
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    private static final Map<String, PositionSourceType> VALUES = new HashMap<>();

    public static PositionSourceType from(String resourceLocation) {
        return VALUES.get(resourceLocation);
    }

    static {
        for (PositionSourceType value : values()) {
            VALUES.put(value.resourceLocation, value);
        }
    }
}
