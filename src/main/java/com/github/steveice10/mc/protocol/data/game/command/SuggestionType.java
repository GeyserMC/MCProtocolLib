package com.github.steveice10.mc.protocol.data.game.command;

import com.github.steveice10.mc.protocol.data.game.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum SuggestionType {
    ASK_SERVER,
    ALL_RECIPES,
    AVAILABLE_SOUNDS,
    SUMMONABLE_ENTITIES;

    private final String resourceLocation;

    SuggestionType() {
        this.resourceLocation = Identifier.formalize(name().toLowerCase(Locale.ROOT));
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    private static final Map<String, SuggestionType> VALUES = new HashMap<>();

    @NotNull
    public static SuggestionType from(String resourceLocation) {
        // Vanilla behavior as of 1.19.3
        // 1.16.5 still has AVAILABLE_BIOMES and vanilla doesn't care
        return VALUES.getOrDefault(resourceLocation, ASK_SERVER);
    }

    static {
        for (SuggestionType suggestionType : values()) {
            VALUES.put(suggestionType.resourceLocation, suggestionType);
        }
    }
}
