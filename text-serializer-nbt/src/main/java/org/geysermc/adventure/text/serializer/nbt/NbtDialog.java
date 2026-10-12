package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import org.cloudburstmc.nbt.NbtMap;

import java.util.Optional;

/**
 * Represents a dialog. This can hold either a {@link Key} reference to a dialog loaded in the {@code "minecraft:dialog"} registry, or an
 * inline-defined dialog serialized as {@link NbtMap}. Instances of this record will always hold one of the two, never both, and never none.
 *
 * @param reference a reference to a dialog loaded in the {@code "minecraft:dialog"} registy
 * @param inline an inline-defined dialog, serialized as {@link NbtMap}
 * @see NbtComponentSerializer
 */
public record NbtDialog(Optional<Key> reference, Optional<NbtMap> inline) implements DialogLike {

    public NbtDialog {
        if (reference.isPresent() == inline.isPresent()) {
            throw new IllegalArgumentException("NbtDialog must either have a reference ID or an inline NbtMap, not none or both!");
        }
    }

    public NbtDialog(Key reference) {
        this(Optional.of(reference), Optional.empty());
    }

    public NbtDialog(NbtMap inline) {
        this(Optional.empty(), Optional.of(inline));
    }
}
