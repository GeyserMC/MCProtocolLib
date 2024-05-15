package org.geysermc.mcprotocollib.protocol.data.game.command;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.intellij.lang.annotations.Subst;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum SuggestionType {
    ASK_SERVER,
    ALL_RECIPES,
    AVAILABLE_SOUNDS,
    SUMMONABLE_ENTITIES;

    private final Key resourceLocation;

    SuggestionType() {
        @Subst("empty") String lowerCase = name().toLowerCase(Locale.ROOT);
        this.resourceLocation = Key.key(lowerCase);
    }

    public Key getResourceLocation() {
        return resourceLocation;
    }

    private static final Map<Key, SuggestionType> VALUES = new HashMap<>();

    @NonNull
    public static SuggestionType from(Key resourceLocation) {
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
