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
    private final @NonNull GameProfile profile;
	private final boolean listed;
	private final int latency;
	private final GameMode gameMode;
	private final @Nullable Component displayName;
	private final UUID sessionId;
	private final long expiresAt;
	private final @Nullable PublicKey publicKey;
	private final byte @Nullable[] keySignature;

	public PlayerListEntry(UUID profileId, GameProfile profile) {
		this(profileId, profile, false, 0, GameMode.SURVIVAL, null, null, 0, null, null);
	}

	public PlayerListEntry(UUID profileId, GameProfile profile, GameMode gameMode) {
		this(profileId, profile, false, 0, gameMode, null, null, 0, null, null);
	}

	public PlayerListEntry(UUID profileId, GameProfile profile, boolean listed) {
		this(profileId, profile, listed, 0, GameMode.SURVIVAL, null, null, 0, null, null);
	}

	public PlayerListEntry(UUID profileId, GameProfile profile, int latency) {
		this(profileId, profile, false, latency, GameMode.SURVIVAL, null, null, 0, null, null);
	}

	public PlayerListEntry(UUID profileId, GameProfile profile, @Nullable Component displayName) {
		this(profileId, profile, false, 0, GameMode.SURVIVAL, displayName, null, 0, null, null);
	}

	public PlayerListEntry(UUID profileId, GameProfile profile, UUID sessionId, long expiresAt, @Nullable PublicKey publicKey, byte @Nullable[] keySignature) {
		this(profileId, new GameProfile(profileId, null), false, 0, GameMode.SURVIVAL, null, sessionId, expiresAt, publicKey, keySignature);
	}
}
