package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.auth.GameProfile;

@Data
@With
@AllArgsConstructor
public class ResolvableProfile {
    private final GameProfile profile;
    private final boolean dynamic;
}
