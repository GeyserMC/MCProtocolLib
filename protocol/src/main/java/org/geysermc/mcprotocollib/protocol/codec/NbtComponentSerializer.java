package org.geysermc.mcprotocollib.protocol.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LazilyParsedNumber;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Taken from <a href="https://github.com/ViaVersion/ViaVersion/blob/4aefc23bb8074303c713a94d7f583ba4020dda04/common/src/main/java/com/viaversion/viaversion/protocols/protocol1_20_3to1_20_2/util/ComponentConverter.java">ViaVersion's ComponentConverter</a>
 */
public class NbtComponentSerializer {

    private static final Set<String> BOOLEAN_TYPES = Set.of(
        "interpret",
        "bold",
        "italic",
        "underlined",
        "strikethrough",
        "obfuscated"
    );
    // Order is important
    private static final List<Pair<String, String>> COMPONENT_TYPES = List.of(
        new Pair<>("text", "text"),
        new Pair<>("translatable", "translate"),
        new Pair<>("score", "score"),
        new Pair<>("selector", "selector"),
        new Pair<>("keybind", "keybind"),
        new Pair<>("nbt", "nbt")
    );

    private NbtComponentSerializer() {

    }

    @Contract("null -> null")
    public static JsonElement tagComponentToJson(@Nullable final Object tag) {
        return convertToJson(null, tag);
    }


    public static @Nullable Object jsonComponentToTag(@Nullable final JsonElement component) {
        return convertToTag(component);
    }

    @Contract("null -> null")
    private static Object convertToTag(final @Nullable JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return null;
        } else if (element.isJsonObject()) {
            final NbtMapBuilder tag = NbtMap.builder();
            final JsonObject jsonObject = element.getAsJsonObject();
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                convertObjectEntry(entry.getKey(), entry.getValue(), tag);
            }

            addComponentType(jsonObject, tag);
            return tag.build();
        } else if (element.isJsonArray()) {
            return convertJsonArray(element.getAsJsonArray());
        } else if (element.isJsonPrimitive()) {
            final JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                return primitive.getAsString();
            } else if (primitive.isBoolean()) {
                return (byte) (primitive.getAsBoolean() ? 1 : 0);
            }

            final Number number = primitive.getAsNumber();
            if (number instanceof Integer) {
                return number.intValue();
            } else if (number instanceof Byte) {
                return number.byteValue();
            } else if (number instanceof Short) {
                return number.shortValue();
            } else if (number instanceof Long) {
                return number.longValue();
            } else if (number instanceof Double) {
                return number.doubleValue();
            } else if (number instanceof Float) {
                return number.floatValue();
            } else if (number instanceof LazilyParsedNumber) {
                // TODO: This might need better handling
                return number.intValue();
            }
            return number.intValue(); // ???
        }
        throw new IllegalArgumentException("Unhandled json type " + element.getClass().getSimpleName() + " with value " + element.getAsString());
    }

    private static <T> void addToListOrFail(final NbtList<T> list, final Object tag) {
        Class<T> listClass = list.getType().getTagClass();

        if (listClass.isInstance(tag)) {
            list.add(listClass.cast(tag));
        } else {
            throw new IllegalArgumentException("Cannot add " + tag.getClass().getSimpleName() + " to list of " + listClass.getSimpleName());
        }
    }

    private static NbtList<?> convertJsonArray(final JsonArray array) {
        // TODO Number arrays?
        NbtList<?> listTag = null;
        boolean singleType = true;
        for (final JsonElement entry : array) {
            final Object convertedEntryTag = convertToTag(entry);
            if (listTag == null) {
                listTag = new NbtList<>(NbtType.byClass(convertedEntryTag.getClass()));
            }

            if (listTag.getType() != NbtType.byClass(convertedEntryTag.getClass())) {
                singleType = false;
                break;
            }

            addToListOrFail(listTag, convertedEntryTag);
        }

        if (singleType) {
            return listTag;
        }

        // Generally, vanilla-esque serializers should not produce this format, so it should be rare
        // Lists are only used for lists of components ("extra" and "with")
        final NbtList<NbtMap> processedListTag = new NbtList<>(NbtType.COMPOUND);
        for (final JsonElement entry : array) {
            final Object convertedTag = convertToTag(entry);
            if (convertedTag instanceof NbtMap nbtMap) {
                processedListTag.add(nbtMap);
                continue;
            }

            // Wrap all entries in compound tags, as lists can only consist of one type of tag
            final NbtMapBuilder compoundTag = NbtMap.builder();
            compoundTag.put("type", "text");
            if (convertedTag instanceof NbtList<?> list) {
                compoundTag.put("text", "");
                compoundTag.put("extra", list);
            } else {
                compoundTag.put("text", stringValue(convertedTag));
            }
            processedListTag.add(compoundTag.build());
        }
        return processedListTag;
    }

    /**
     * Converts a json object entry to a tag entry.
     *
     * @param key   key of the entry
     * @param value value of the entry
     * @param tag   the resulting compound tag
     */
    private static void convertObjectEntry(final String key, final JsonElement value, final NbtMapBuilder tag) {
        if ((key.equals("contents")) && value.isJsonObject()) {
            // Store show_entity id as int array instead of uuid string
            // Not really required, but we might as well make it more compact
            final JsonObject hoverEvent = value.getAsJsonObject();
            final JsonElement id = hoverEvent.get("id");
            final UUID uuid;
            if (id != null && id.isJsonPrimitive() && (uuid = parseUUID(id.getAsString())) != null) {
                hoverEvent.remove("id");

                final NbtMapBuilder convertedTag = ((NbtMap) convertToTag(value)).toBuilder();
                convertedTag.put("id", toIntArray(uuid));
                tag.put(key, convertedTag.build());
                return;
            }
        }

        tag.put(key, convertToTag(value));
    }

    private static void addComponentType(final JsonObject object, final NbtMapBuilder tag) {
        if (object.has("type")) {
            return;
        }

        // Add the type to speed up deserialization and make DFU errors slightly more useful
        for (final Pair<String, String> pair : COMPONENT_TYPES) {
            if (object.has(pair.value)) {
                tag.put("type", pair.key);
                return;
            }
        }
    }

    private static @Nullable JsonElement convertToJson(final @Nullable String key, final @Nullable Object tag) {
        if (tag == null) {
            return null;
        } else if (tag instanceof NbtMap nbtMap) {
            final JsonObject object = new JsonObject();
            if (!"value".equals(key)) {
                removeComponentType(object);
            }

            for (Map.Entry<String, Object> entry : nbtMap.entrySet()) {
                convertNbtMapEntry(entry.getKey(), entry.getValue(), object);
            }
            return object;
        } else if (tag instanceof NbtList<?> list) {
            final JsonArray array = new JsonArray();
            for (final Object listEntry : list) {
                array.add(convertToJson(null, listEntry));
            }
            return array;
        } else if (tag instanceof Number number) {
            if (key != null && BOOLEAN_TYPES.contains(key)) {
                // Booleans don't have a direct representation in nbt
                return new JsonPrimitive(number.byteValue() != 0);
            }
            return new JsonPrimitive(number);
        } else if (tag instanceof String string) {
            return new JsonPrimitive(string);
        } else if (tag instanceof byte[] arrayTag) {
            final JsonArray array = new JsonArray();
            for (final byte num : arrayTag) {
                array.add(num);
            }
            return array;
        } else if (tag instanceof int[] arrayTag) {
            final JsonArray array = new JsonArray();
            for (final int num : arrayTag) {
                array.add(num);
            }
            return array;
        } else if (tag instanceof long[] arrayTag) {
            final JsonArray array = new JsonArray();
            for (final long num : arrayTag) {
                array.add(num);
            }
            return array;
        }
        throw new IllegalArgumentException("Unhandled tag type " + tag.getClass().getSimpleName());
    }

    private static void convertNbtMapEntry(final String key, final Object tag, final JsonObject object) {
        if ((key.equals("contents")) && tag instanceof NbtMap showEntity) {
            // Back to a UUID string
            final Object idTag = showEntity.get("id");
            if (idTag instanceof int[] array) {
                final JsonObject convertedElement = (JsonObject) convertToJson(key, tag);
                final UUID uuid = fromIntArray(array);
                convertedElement.addProperty("id", uuid.toString());
                object.add(key, convertedElement);
                return;
            }
        }

        // "":1 is a valid tag, but not a valid json component
        object.add(key.isEmpty() ? "text" : key, convertToJson(key, tag));
    }

    private static void removeComponentType(final JsonObject object) {
        final JsonElement type = object.remove("type");
        if (type == null || !type.isJsonPrimitive()) {
            return;
        }

        // Remove the other fields
        final String typeString = type.getAsString();
        for (final Pair<String, String> pair : COMPONENT_TYPES) {
            if (!pair.key.equals(typeString)) {
                object.remove(pair.value);
            }
        }
    }

    // Last adopted from https://github.com/ViaVersion/ViaVersion/blob/8e38e25cbad1798abb628b4994f4047eaf64640d/common/src/main/java/com/viaversion/viaversion/util/UUIDUtil.java
    public static UUID fromIntArray(final int[] parts) {
        if (parts.length != 4) {
            return new UUID(0, 0);
        }
        return new UUID((long) parts[0] << 32 | (parts[1] & 0xFFFFFFFFL), (long) parts[2] << 32 | (parts[3] & 0xFFFFFFFFL));
    }

    public static int[] toIntArray(final UUID uuid) {
        return toIntArray(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
    }

    public static int[] toIntArray(final long msb, final long lsb) {
        return new int[]{(int) (msb >> 32), (int) msb, (int) (lsb >> 32), (int) lsb};
    }

    public static @Nullable UUID parseUUID(final String uuidString) {
        try {
            return UUID.fromString(uuidString);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    // Last adopted from https://github.com/ViaVersion/ViaNBT/commit/ad8ac024c48c2fc25e18dc689b3ca62602420ab9
    private static String stringValue(Object tag) {
        if (tag instanceof byte[] bytes) {
            return Arrays.toString(bytes);
        } else if (tag instanceof Byte byteTag) {
            return Byte.toString(byteTag);
        } else if (tag instanceof Double doubleTag) {
            return Double.toString(doubleTag);
        } else if (tag instanceof Float floatTag) {
            return Float.toString(floatTag);
        } else if (tag instanceof int[] intArray) {
            return Arrays.toString(intArray);
        } else if (tag instanceof Integer integer) {
            return Integer.toString(integer);
        } else if (tag instanceof long[] longs) {
            return Arrays.toString(longs);
        } else if (tag instanceof Long longTag) {
            return Long.toString(longTag);
        } else if (tag instanceof Short shortTag) {
            return Short.toString(shortTag);
        } else if (tag instanceof String string) {
            return string;
        } else {
            return tag.toString();
        }
    }

    // Implemented in the same way as ViaVersion's custom Pair class in order to reduce diff
    @AllArgsConstructor
    private static class Pair<K, V> {
        private final K key;
        private final V value;
    }
}
