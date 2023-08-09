package com.github.steveice10.mc.protocol.data.game;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.security.PublicKey;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerListEntry {
    private final @NonNull UUID profileId;
    private @Nullable GameProfile profile;
    private boolean listed;
    private int latency;
    private GameMode gameMode;
    private @Nullable Component displayName;
    private UUID sessionId;
    private long expiresAt;
    private @Nullable PublicKey publicKey;
    private byte @Nullable[] keySignature;

    public PlayerListEntry(UUID profileId) {
        this(profileId, null, false, 0, GameMode.SURVIVAL, null, null, 0, null, null);
    }
}
