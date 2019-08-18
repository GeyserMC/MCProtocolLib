package com.github.steveice10.mc.protocol.data.status;

import com.github.steveice10.mc.protocol.data.message.Message;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class ServerStatusInfo {
    private @NonNull VersionInfo versionInfo;
    private @NonNull PlayerInfo playerInfo;
    private @NonNull Message description;
    private BufferedImage icon;
}
