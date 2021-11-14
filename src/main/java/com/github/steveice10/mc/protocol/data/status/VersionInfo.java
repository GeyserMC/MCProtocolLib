package com.github.steveice10.mc.protocol.data.status;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class VersionInfo {
    private @NonNull String versionName;
    private int protocolVersion;
}
