package com.github.steveice10.mc.protocol.data.game.level.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class MapData {
    private final int columns;
    private final int rows;
    private final int x;
    private final int y;
    private final @NonNull byte data[];
}
