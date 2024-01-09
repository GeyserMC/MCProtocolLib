package org.geysermc.mcprotocollib.protocol.data.game.chunk;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChunkBiomeData {
    private final int x;
    private final int z;
    private final byte[] buffer;
}
