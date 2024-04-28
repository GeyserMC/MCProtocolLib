package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cloudburstmc.nbt.NbtMap;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
public class AdventureModePredicate {
    private final List<BlockPredicate> predicates;
    private final boolean showInTooltip;

    @Data
    @AllArgsConstructor
    public static class BlockPredicate {
        private final @Nullable String location;
        private final int @Nullable[] holders;
        private final @Nullable List<PropertyMatcher> properties;
        private final @Nullable NbtMap nbt;
    }

    @Data
    @AllArgsConstructor
    public static class PropertyMatcher {
        private final String name;
        private final @Nullable String value;
        private final @Nullable String minValue;
        private final @Nullable String maxValue;
    }
}
