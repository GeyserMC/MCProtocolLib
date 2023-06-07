package com.github.steveice10.mc.protocol.data.game.recipe;

import com.github.steveice10.mc.protocol.data.game.Identifier;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum RecipeType {
    CRAFTING_SHAPED,
    CRAFTING_SHAPELESS,
    CRAFTING_SPECIAL_ARMORDYE,
    CRAFTING_SPECIAL_BOOKCLONING,
    CRAFTING_SPECIAL_MAPCLONING,
    CRAFTING_SPECIAL_MAPEXTENDING,
    CRAFTING_SPECIAL_FIREWORK_ROCKET,
    CRAFTING_SPECIAL_FIREWORK_STAR,
    CRAFTING_SPECIAL_FIREWORK_STAR_FADE,
    CRAFTING_SPECIAL_TIPPEDARROW,
    CRAFTING_SPECIAL_BANNERDUPLICATE,
    CRAFTING_SPECIAL_SHIELDDECORATION,
    CRAFTING_SPECIAL_SHULKERBOXCOLORING,
    CRAFTING_SPECIAL_SUSPICIOUSSTEW,
    CRAFTING_SPECIAL_REPAIRITEM,
    SMELTING,
    BLASTING,
    SMOKING,
    CAMPFIRE_COOKING,
    STONECUTTING,
    SMITHING_TRANSFORM,
    SMITHING_TRIM,
    CRAFTING_DECORATED_POT;

    private final String resourceLocation;

    RecipeType() {
        this.resourceLocation = Identifier.formalize(name().toLowerCase(Locale.ROOT));
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    private static final Map<String, RecipeType> VALUES = new HashMap<>();

    public static RecipeType from(String resourceLocation) {
        return VALUES.get(resourceLocation);
    }

    static {
        for (RecipeType type : values()) {
            VALUES.put(type.resourceLocation, type);
        }
    }
}
