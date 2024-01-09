package org.geysermc.mcprotocollib.protocol.data.game.entity.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericObjectData implements ObjectData {
    private final int value;
}
