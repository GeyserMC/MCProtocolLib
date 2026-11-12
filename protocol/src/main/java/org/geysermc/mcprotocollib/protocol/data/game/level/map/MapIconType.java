package org.geysermc.mcprotocollib.protocol.data.game.level.map;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

public enum MapIconType {
    PLAYER,
    FRAME,
    RED_MARKER,
    BLUE_MARKER,
    TARGET_X,
    TARGET_POINT,
    PLAYER_OFF_MAP,
    PLAYER_OFF_LIMITS,
    MANSION,
    MONUMENT,
    BANNER_WHITE,
    BANNER_ORANGE,
    BANNER_MAGENTA,
    BANNER_LIGHT_BLUE,
    BANNER_YELLOW,
    BANNER_LIME,
    BANNER_PINK,
    BANNER_GRAY,
    BANNER_LIGHT_GRAY,
    BANNER_CYAN,
    BANNER_PURPLE,
    BANNER_BLUE,
    BANNER_BROWN,
    BANNER_GREEN,
    BANNER_RED,
    BANNER_BLACK,
    RED_X,
    VILLAGE_DESERT,
    VILLAGE_PLAINS,
    VILLAGE_SAVANNA,
    VILLAGE_SNOWY,
    VILLAGE_TAIGA,
    JUNGLE_TEMPLE,
    SWAMP_HUT,
    TRIAL_CHAMBERS,
    ABANDONED_CAMP,
    ANCIENT_CITY,
    DESERT_PYRAMID,
    MINESHAFT,
    OCEAN_RUIN_WARM;

    private static final MapIconType[] VALUES = values();

    @Getter
    @Accessors(fluent = true)
    private final Key key;

    MapIconType() {
        this.key = Key.key("minecraft", name().toLowerCase(Locale.ROOT));
    }

    public static @Nullable MapIconType from(int id) {
        if (id >= 0 && id < VALUES.length) {
            return VALUES[id];
        } else {
            return null;
        }
    }

    public static @Nullable MapIconType from(Key key) {
        for (MapIconType type : VALUES) {
            if (type.key.equals(key)) {
                return type;
            }
        }
        return null;
    }
}
