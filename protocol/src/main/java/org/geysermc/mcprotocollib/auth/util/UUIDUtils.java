package org.geysermc.mcprotocollib.auth.util;

import java.util.HexFormat;
import java.util.UUID;

public class UUIDUtils {
    public static UUID convertToDashed(String noDashes) {
        if (noDashes == null) {
            return null;
        }

        return new UUID(
            HexFormat.fromHexDigitsToLong(noDashes, 0, 16),
            HexFormat.fromHexDigitsToLong(noDashes, 16, 32)
        );
    }

    public static String convertToNoDashes(UUID uuid) {
        if (uuid == null) {
            return null;
        }

        return uuid.toString().replace("-", "");
    }
}
