package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import org.cloudburstmc.nbt.NbtMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class NbtDialogTest {

    @Test
    public void testEmptyOrBothNbtDialog() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new NbtDialog(Optional.empty(), Optional.empty()),
            "NbtDialog constructor must throw IllegalArgumentException when there is no reference and no inline dialog");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new NbtDialog(Optional.of(Key.key("quick_actions")), Optional.of(NbtMap.EMPTY)),
            "NbtDialog constructor must throw IllegalArgumentException when there is both a reference and an inline dialog");
    }
}
