package org.geysermc.mcprotocollib.protocol.data.game.level.block.value;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChestValue implements BlockValue {
    private final int viewers;
}
