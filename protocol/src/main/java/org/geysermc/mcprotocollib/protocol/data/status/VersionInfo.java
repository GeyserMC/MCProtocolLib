package org.geysermc.mcprotocollib.protocol.data.status;

import lombok.*;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class VersionInfo {
    private @NonNull String versionName;
    private int protocolVersion;
}
