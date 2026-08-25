package org.geysermc.adventure.text.serializer.nbt;

import java.util.UUID;

final class NbtSerializationUtil {

    static UUID deserializeLenientUUID(Object object) {
        if (object instanceof int[] array) {
            if (array.length != 4) {
                return new UUID(0, 0);
            }
            return new UUID((long) array[0] << 32 | (array[1] & 0xFFFFFFFFL), (long) array[2] << 32 | (array[3] & 0xFFFFFFFFL));
        } else if (object instanceof String string) {
            return UUID.fromString(string);
        }
        throw new IllegalArgumentException("Don't know how to leniently parse UUID: " + object);
    }

    static int[] serializeUUID(UUID uuid) {
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();
        return new int[]{(int) (mostSignificantBits >> 32), (int) mostSignificantBits, (int) (leastSignificantBits >> 32), (int) leastSignificantBits};
    }
}
