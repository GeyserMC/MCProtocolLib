package org.geysermc.mcprotocollib.protocol.data.game.level.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cloudburstmc.math.vector.Vector3i;

@Data
@AllArgsConstructor
public class BlockChangeEntry {
    private final Vector3i position;
    private final int block;
}
