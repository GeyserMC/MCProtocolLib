package org.geysermc.mcprotocollib.protocol.data.game.item;

import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentType;

import java.util.Map;
import java.util.Set;

public record HashedStack(int id, int count, Map<DataComponentType<?>, Integer> addedComponents, Set<DataComponentType<?>> removedComponents) {
}
