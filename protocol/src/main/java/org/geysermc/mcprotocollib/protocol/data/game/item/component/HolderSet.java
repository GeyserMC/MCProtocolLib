package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Function;

/**
 * Represents a set of holders that could either be explicit, or resolved from a tag location.
 * The client has to know how to resolve the tag location to get the holders.
 */
public record HolderSet(@Nullable String location, int @Nullable [] holders) {
    public HolderSet {
        if (location == null && holders == null) {
            throw new IllegalArgumentException("Either location or holders must be set");
        } else if (location != null && holders != null) {
            throw new IllegalArgumentException("Only one of location or holders can be set");
        }
    }

    /**
     * Return either the explicit holders, or resolve the tag location to get the holders.
     *
     * @param tagResolver The function to resolve the tag location to get the holders.
     * @return The holders.
     */
    public int[] resolve(Function<String, int[]> tagResolver) {
        if (holders != null) {
            return holders;
        }

        return tagResolver.apply(location);
    }
}
