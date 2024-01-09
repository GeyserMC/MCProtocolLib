package org.geysermc.mcprotocollib.protocol.data.status;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import net.kyori.adventure.text.Component;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class ServerStatusInfo {
    private @NonNull VersionInfo versionInfo;
    private @NonNull PlayerInfo playerInfo;
    private @NonNull Component description;
    private byte[] iconPng;
    private boolean enforcesSecureChat;
}
