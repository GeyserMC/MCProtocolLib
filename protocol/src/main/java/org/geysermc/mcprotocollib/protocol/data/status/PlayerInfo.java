package org.geysermc.mcprotocollib.protocol.data.status;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.geysermc.mcprotocollib.auth.GameProfile;

import java.util.List;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class PlayerInfo {
    private int maxPlayers;
    private int onlinePlayers;
    private @NonNull List<GameProfile> players;
}
