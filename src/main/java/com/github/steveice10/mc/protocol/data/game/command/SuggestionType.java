package com.github.steveice10.mc.protocol.data.game.command;

import com.github.steveice10.mc.protocol.data.game.Identifier;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
public enum SuggestionType {
    ASK_SERVER,
    ALL_RECIPES,
    AVAILABLE_SOUNDS,
    SUMMONABLE_ENTITIES;

    private static final Map<String, SuggestionType> VALUES = new HashMap<>();

    static {
        for (SuggestionType suggestionType : values()) {
            VALUES.put(suggestionType.resourceLocation, suggestionType);
        }
    }

    private final String resourceLocation;

    SuggestionType() {
        this.resourceLocation = Identifier.formalize(name().toLowerCase(Locale.ROOT));
    }

    @NotNull
    public static SuggestionType from(String resourceLocation) {
        // Vanilla behavior as of 1.19.3
        // 1.16.5 still has AVAILABLE_BIOMES and vanilla doesn't care
        return VALUES.getOrDefault(resourceLocation, ASK_SERVER);
    }

}
