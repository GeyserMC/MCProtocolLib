package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Data;
import lombok.NonNull;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Function;

/**
 * Represents a set of holders that could either be explicit, or resolved from a tag location.
 * The client has to know how to resolve the tag location to get the holders.
 */
@Data
public final class HolderSet {
    private final @Nullable Key location;
    private final @Nullable IntList holders;

    public HolderSet(@NonNull IntList holders) {
        this.location = null;
        this.holders = new IntArrayList(holders);
    }

    public HolderSet(@NonNull Key location) {
        this.location = location;
        this.holders = null;
    }

    /**
     * Return either the explicit holders, or resolve the tag location to get the holders.
     *
     * @param tagResolver The function to resolve the tag location to get the holders.
     * @return The holders.
     */
    public IntList resolve(Function<Key, IntList> tagResolver) {
        if (holders != null) {
            return holders;
        }

        return tagResolver.apply(location);
    }

    @Override
    public String toString() {
        if (this.holders != null) {
            return getClass().getSimpleName() + "[holders=" + this.holders + "]";
        } else {
            return getClass().getSimpleName() + "[location=" + this.location + "]";
        }
    }
}
