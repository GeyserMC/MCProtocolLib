package com.github.steveice10.mc.protocol.data.status;

import com.github.steveice10.mc.auth.data.GameProfile;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class PlayerInfo {
    private int maxPlayers;
    private int onlinePlayers;
    private @Nullable List<GameProfile> players;
}
