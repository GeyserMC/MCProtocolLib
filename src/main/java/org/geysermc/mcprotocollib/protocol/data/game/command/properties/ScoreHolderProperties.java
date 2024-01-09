package org.geysermc.mcprotocollib.protocol.data.game.command.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreHolderProperties implements CommandProperties {
    private final boolean allowMultiple;
}
