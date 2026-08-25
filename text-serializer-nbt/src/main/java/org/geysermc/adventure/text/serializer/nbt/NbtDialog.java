package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import org.cloudburstmc.nbt.NbtMap;

import java.util.Optional;

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
