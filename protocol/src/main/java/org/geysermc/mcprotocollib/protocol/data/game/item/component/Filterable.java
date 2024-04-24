package com.github.steveice10.mc.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class Filterable<T> {
    private final T raw;
    private final @Nullable T optional;
}
