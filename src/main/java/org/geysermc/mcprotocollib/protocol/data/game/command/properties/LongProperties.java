package org.geysermc.mcprotocollib.protocol.data.game.command.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LongProperties implements CommandProperties {
    private final long min;
    private final long max;
}
