package org.geysermc.mcprotocollib.protocol.data.game.level.sound;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomSound implements Sound {
    private final String name;
    private final boolean newSystem;
    private final float range;
}
