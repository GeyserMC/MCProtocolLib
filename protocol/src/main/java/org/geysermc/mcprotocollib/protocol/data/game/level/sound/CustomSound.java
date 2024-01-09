package org.geysermc.mcprotocollib.protocol.data.game.level.sound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class CustomSound implements Sound {
    private final @NonNull String name;
    private final boolean newSystem;
    private final float range;
}
