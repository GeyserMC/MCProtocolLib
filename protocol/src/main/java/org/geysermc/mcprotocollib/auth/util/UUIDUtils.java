package org.geysermc.mcprotocollib.auth.util;

import java.util.UUID;

public class UUIDUtils {
    public static UUID convertToDashed(String noDashes) {
        if (noDashes == null) {
            return null;
        }

        StringBuilder idBuff = new StringBuilder(noDashes);
        idBuff.insert(20, '-');
        idBuff.insert(16, '-');
        idBuff.insert(12, '-');
        idBuff.insert(8, '-');
        return UUID.fromString(idBuff.toString());
    }

    public static String convertToNoDashes(UUID uuid) {
        if (uuid == null) {
            return null;
        }

        return uuid.toString().replace("-", "");
    }
}
