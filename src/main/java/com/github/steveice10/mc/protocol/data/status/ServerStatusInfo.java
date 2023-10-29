package com.github.steveice10.mc.protocol.data.status;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public record ServerStatusInfo(@Nullable Component description,
                               @Nullable PlayerInfo playerInfo,
                               @Nullable VersionInfo versionInfo,
                               byte @Nullable [] iconPng,
                               @Nullable Boolean enforcesSecureChat) {
}
