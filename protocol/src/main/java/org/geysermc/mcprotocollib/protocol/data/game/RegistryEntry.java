package org.geysermc.mcprotocollib.protocol.data.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.key.Key;
import org.cloudburstmc.nbt.NbtMap;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class RegistryEntry {
    private final Key id;
    private final @Nullable NbtMap data;
}
