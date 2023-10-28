package com.github.steveice10.mc.protocol.data.status;

import lombok.*;
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
