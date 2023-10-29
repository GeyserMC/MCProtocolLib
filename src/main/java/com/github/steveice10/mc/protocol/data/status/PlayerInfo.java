package com.github.steveice10.mc.protocol.data.status;

import com.github.steveice10.mc.auth.data.GameProfile;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record PlayerInfo(int maxPlayers, int onlinePlayers, @Nullable List<GameProfile> players) {
}
