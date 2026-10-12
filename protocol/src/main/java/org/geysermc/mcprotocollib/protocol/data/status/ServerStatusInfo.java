package org.geysermc.mcprotocollib.protocol.data.status;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class ServerStatusInfo {
    private Component description;
    private @Nullable PlayerInfo playerInfo;
    private @Nullable VersionInfo versionInfo;
    private byte[] iconPng;
    private boolean enforcesSecureChat;
}
