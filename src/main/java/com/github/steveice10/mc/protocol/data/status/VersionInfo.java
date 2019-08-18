package com.github.steveice10.mc.protocol.data.status;

import com.github.steveice10.mc.protocol.MinecraftConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class VersionInfo {
    public static final VersionInfo CURRENT = new VersionInfo(MinecraftConstants.GAME_VERSION, MinecraftConstants.PROTOCOL_VERSION);

    private @NonNull String versionName;
    private int protocolVersion;
}
