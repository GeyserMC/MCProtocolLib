package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Data;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Function;

/**
 * Represents a set of holders that could either be explicit, or resolved from a tag location.
 * The client has to know how to resolve the tag location to get the holders.
 */
@Data
public final class HolderSet {
    private final @Nullable String location;
    private final int @Nullable [] holders;

    public HolderSet(int @NonNull [] holders) {
        this.location = null;
        this.holders = holders;
    }

    public HolderSet(@NonNull String location) {
        this.location = location;
        this.holders = null;
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
