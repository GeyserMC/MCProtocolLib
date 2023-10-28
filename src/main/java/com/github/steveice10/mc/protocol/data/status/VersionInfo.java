package com.github.steveice10.mc.protocol.data.status;

import lombok.*;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class VersionInfo {
    private @NonNull String versionName;
    private int protocolVersion;
}
