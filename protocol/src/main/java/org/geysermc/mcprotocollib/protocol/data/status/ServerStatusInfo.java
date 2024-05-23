package org.geysermc.mcprotocollib.protocol.data.status;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class ServerStatusInfo {
    private @Nullable VersionInfo versionInfo;
    private @Nullable PlayerInfo playerInfo;
    private @NonNull Component description;
    private byte[] iconPng;
    private boolean enforcesSecureChat;
}
