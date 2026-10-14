package org.geysermc.mcprotocollib.auth.util;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.HexFormat;
import java.util.UUID;

public class UUIDUtils {

    @Contract("null -> null; !null -> !null")
    public static @Nullable UUID convertToDashed(@Nullable String noDashes) {
        if (noDashes == null) {
            return null;
        }

        return new UUID(
            HexFormat.fromHexDigitsToLong(noDashes, 0, 16),
            HexFormat.fromHexDigitsToLong(noDashes, 16, 32)
        );
    }

    @Contract("null -> null; !null -> !null")
    public static @Nullable String convertToNoDashes(@Nullable UUID uuid) {
        if (uuid == null) {
            return null;
        }

        return uuid.toString().replace("-", "");
    }
}
