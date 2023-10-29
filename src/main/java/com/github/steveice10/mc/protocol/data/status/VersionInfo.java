package com.github.steveice10.mc.protocol.data.status;

import org.jetbrains.annotations.NotNull;

public record VersionInfo(@NotNull String versionName, int protocolVersion) {
}
