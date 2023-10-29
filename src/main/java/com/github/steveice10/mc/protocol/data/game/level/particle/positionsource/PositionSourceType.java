package com.github.steveice10.mc.protocol.data.game.level.particle.positionsource;

import com.github.steveice10.mc.protocol.data.game.Identifier;
import lombok.Getter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
public enum PositionSourceType {
    BLOCK,
    ENTITY;

    private static final Map<String, PositionSourceType> VALUES = new HashMap<>();

    static {
        for (PositionSourceType value : values()) {
            VALUES.put(value.resourceLocation, value);
        }
    }

    private final String resourceLocation = Identifier.formalize(name().toLowerCase(Locale.ROOT));

    public static PositionSourceType from(String resourceLocation) {
        return VALUES.get(resourceLocation);
    }
}
