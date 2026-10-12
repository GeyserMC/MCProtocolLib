package org.geysermc.mcprotocollib.protocol.data.game.level.map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MapData {
    private final int columns;
    private final int rows;
    private final int x;
    private final int y;
    private final byte [] data;
}
