package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.Nullable;

@Data
@With
@Builder(toBuilder = true)
@AllArgsConstructor
public class Filterable<T> {
    private final T raw;
    private final @Nullable T optional;
}
