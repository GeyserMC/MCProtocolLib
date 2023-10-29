package com.github.steveice10.mc.protocol.data.status;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public record ServerStatusInfo(@Nullable Component description,
                               @Nullable PlayerInfo playerInfo,
                               @Nullable VersionInfo versionInfo,
                               byte @Nullable [] iconPng,
                               @Nullable Boolean enforcesSecureChat) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerStatusInfo that)) return false;
        return Objects.equals(description, that.description)
                && Objects.equals(playerInfo, that.playerInfo)
                && Objects.equals(versionInfo, that.versionInfo)
                && Arrays.equals(iconPng, that.iconPng)
                && Objects.equals(enforcesSecureChat, that.enforcesSecureChat);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(description, playerInfo, versionInfo, enforcesSecureChat);
        result = 31 * result + Arrays.hashCode(iconPng);
        return result;
    }
}
