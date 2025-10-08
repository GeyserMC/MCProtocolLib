package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import org.cloudburstmc.nbt.NbtMap;

@Builder(toBuilder = true)
public record TypedEntityData<T>(T type, NbtMap tag) {
    // TODO: Improve this implementation, too bulky in DataComponentTypes
}
