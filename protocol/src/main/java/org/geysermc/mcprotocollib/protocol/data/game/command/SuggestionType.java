package org.geysermc.mcprotocollib.protocol.data.game.command;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.ResourceLocation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum SuggestionType {
    ASK_SERVER,
    ALL_RECIPES,
    AVAILABLE_SOUNDS,
    SUMMONABLE_ENTITIES;

    private final ResourceLocation resourceLocation;

    SuggestionType() {
        this.resourceLocation = ResourceLocation.fromString(name().toLowerCase(Locale.ROOT));
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    private static final Map<ResourceLocation, SuggestionType> VALUES = new HashMap<>();

    @NonNull
    public static SuggestionType from(ResourceLocation resourceLocation) {
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
