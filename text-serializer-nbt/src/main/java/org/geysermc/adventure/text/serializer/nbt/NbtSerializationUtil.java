package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.util.ARGBLike;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtType;

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

    static ARGBLike deserializeARGB(Object object) {
        if (object instanceof NbtList<?> list) {
            if (list.size() != 4 || list.getType() != NbtType.FLOAT) {
                throw new IllegalArgumentException("ARGB vector must contain exactly 4 floats, got: " + list);
            }
            //noinspection unchecked
            NbtList<Float> floats = (NbtList<Float>) list;
            return ShadowColor.shadowColor(ratioFloatToByte(floats.get(0)), ratioFloatToByte(floats.get(1)), ratioFloatToByte(floats.get(2)), ratioFloatToByte(floats.get(3)));
        } else if (object instanceof Integer integer) {
            return ShadowColor.shadowColor(integer);
        }
        throw new IllegalArgumentException("Don't know how to parse ARGB colour: " + object);
    }

    private static int ratioFloatToByte(float f) {
        return (int) Math.floor(f * 255.0F);
    }
}
