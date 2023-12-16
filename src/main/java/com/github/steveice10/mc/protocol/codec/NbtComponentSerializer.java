package com.github.steveice10.mc.protocol.codec;

import com.github.steveice10.opennbt.tag.builtin.ByteArrayTag;
import com.github.steveice10.opennbt.tag.builtin.ByteTag;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.DoubleTag;
import com.github.steveice10.opennbt.tag.builtin.FloatTag;
import com.github.steveice10.opennbt.tag.builtin.IntArrayTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.LongArrayTag;
import com.github.steveice10.opennbt.tag.builtin.LongTag;
import com.github.steveice10.opennbt.tag.builtin.ShortTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LazilyParsedNumber;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

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

    private static final Set<String> BOOLEAN_TYPES = new HashSet<>(Arrays.asList(
        "interpret",
        "bold",
        "italic",
        "underlined",
        "strikethrough",
        "obfuscated"
    ));
    // Order is important
    private static final List<Pair<String, String>> COMPONENT_TYPES = Arrays.asList(
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
    public static JsonElement tagComponentToJson(@Nullable final Tag tag) {
        return convertToJson(null, tag);
    }


    public static @Nullable Tag jsonComponentToTag(@Nullable final JsonElement component) {
        return convertToTag("", component);
    }

    @Contract("_, null -> null")
    private static Tag convertToTag(String name, final @Nullable JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return null;
        } else if (element.isJsonObject()) {
            final CompoundTag tag = new CompoundTag(name);
            final JsonObject jsonObject = element.getAsJsonObject();
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                convertObjectEntry(entry.getKey(), entry.getValue(), tag);
            }

            addComponentType(jsonObject, tag);
            return tag;
        } else if (element.isJsonArray()) {
            return convertJsonArray(name, element.getAsJsonArray());
        } else if (element.isJsonPrimitive()) {
            final JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                return new StringTag(name, primitive.getAsString());
            } else if (primitive.isBoolean()) {
                return new ByteTag(name, (byte) (primitive.getAsBoolean() ? 1 : 0));
            }

            final Number number = primitive.getAsNumber();
            if (number instanceof Integer) {
                return new IntTag(name, number.intValue());
            } else if (number instanceof Byte) {
                return new ByteTag(name, number.byteValue());
            } else if (number instanceof Short) {
                return new ShortTag(name, number.shortValue());
            } else if (number instanceof Long) {
                return new LongTag(name, number.longValue());
            } else if (number instanceof Double) {
                return new DoubleTag(name, number.doubleValue());
            } else if (number instanceof Float) {
                return new FloatTag(name, number.floatValue());
            } else if (number instanceof LazilyParsedNumber) {
                // TODO: This might need better handling
                return new IntTag(name, number.intValue());
            }
            return new IntTag(name, number.intValue()); // ???
        }
        throw new IllegalArgumentException("Unhandled json type " + element.getClass().getSimpleName() + " with value " + element.getAsString());
    }

    private static ListTag convertJsonArray(String name, final JsonArray array) {
        // TODO Number arrays?
        final ListTag listTag = new ListTag(name);
        boolean singleType = true;
        for (final JsonElement entry : array) {
            final Tag convertedEntryTag = convertToTag("", entry);
            if (listTag.getElementType() != null && listTag.getElementType() != convertedEntryTag.getClass()) {
                singleType = false;
                break;
            }

            listTag.add(convertedEntryTag);
        }

        if (singleType) {
            return listTag;
        }

        // Generally, vanilla-esque serializers should not produce this format, so it should be rare
        // Lists are only used for lists of components ("extra" and "with")
        final ListTag processedListTag = new ListTag(name);
        for (final JsonElement entry : array) {
            final Tag convertedTag = convertToTag("", entry);
            if (convertedTag instanceof CompoundTag) {
                processedListTag.add(convertedTag);
                continue;
            }

            // Wrap all entries in compound tags, as lists can only consist of one type of tag
            final CompoundTag compoundTag = new CompoundTag("");
            compoundTag.put(new StringTag("type", "text"));
            if (convertedTag instanceof ListTag) {
                compoundTag.put(new StringTag("text"));
                compoundTag.put(new ListTag("extra", ((ListTag) convertedTag).getValue()));
            } else {
                compoundTag.put(new StringTag("text", stringValue(convertedTag)));
            }
            processedListTag.add(compoundTag);
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
    private static void convertObjectEntry(final String key, final JsonElement value, final CompoundTag tag) {
        if ((key.equals("contents")) && value.isJsonObject()) {
            // Store show_entity id as int array instead of uuid string
            // Not really required, but we might as well make it more compact
            final JsonObject hoverEvent = value.getAsJsonObject();
            final JsonElement id = hoverEvent.get("id");
            final UUID uuid;
            if (id != null && id.isJsonPrimitive() && (uuid = parseUUID(id.getAsString())) != null) {
                hoverEvent.remove("id");

                final CompoundTag convertedTag = (CompoundTag) convertToTag(key, value);
                convertedTag.put(new IntArrayTag("id", toIntArray(uuid)));
                tag.put(convertedTag);
                return;
            }
        }

        tag.put(convertToTag(key, value));
    }

    private static void addComponentType(final JsonObject object, final CompoundTag tag) {
        if (object.has("type")) {
            return;
        }

        // Add the type to speed up deserialization and make DFU errors slightly more useful
        for (final Pair<String, String> pair : COMPONENT_TYPES) {
            if (object.has(pair.value)) {
                tag.put(new StringTag("type", pair.key));
                return;
            }
        }
    }

    private static @Nullable JsonElement convertToJson(final @Nullable String key, final @Nullable Tag tag) {
        if (tag == null) {
            return null;
        } else if (tag instanceof CompoundTag) {
            final JsonObject object = new JsonObject();
            if (!"value".equals(key)) {
                removeComponentType(object);
            }

            for (final Tag entry : ((CompoundTag) tag)) {
                convertCompoundTagEntry(entry.getName(), entry, object);
            }
            return object;
        } else if (tag instanceof ListTag) {
            final ListTag list = (ListTag) tag;
            final JsonArray array = new JsonArray();
            for (final Tag listEntry : list) {
                array.add(convertToJson(null, listEntry));
            }
            return array;
        } else if (tag.getValue() instanceof Number) {
            final Number number = (Number) tag.getValue();
            if (key != null && BOOLEAN_TYPES.contains(key)) {
                // Booleans don't have a direct representation in nbt
                return new JsonPrimitive(number.byteValue() != 0);
            }
            return new JsonPrimitive(number);
        } else if (tag instanceof StringTag) {
            return new JsonPrimitive(((StringTag) tag).getValue());
        } else if (tag instanceof ByteArrayTag) {
            final ByteArrayTag arrayTag = (ByteArrayTag) tag;
            final JsonArray array = new JsonArray();
            for (final byte num : arrayTag.getValue()) {
                array.add(num);
            }
            return array;
        } else if (tag instanceof IntArrayTag) {
            final IntArrayTag arrayTag = (IntArrayTag) tag;
            final JsonArray array = new JsonArray();
            for (final int num : arrayTag.getValue()) {
                array.add(num);
            }
            return array;
        } else if (tag instanceof LongArrayTag) {
            final LongArrayTag arrayTag = (LongArrayTag) tag;
            final JsonArray array = new JsonArray();
            for (final long num : arrayTag.getValue()) {
                array.add(num);
            }
            return array;
        }
        throw new IllegalArgumentException("Unhandled tag type " + tag.getClass().getSimpleName());
    }

    private static void convertCompoundTagEntry(final String key, final Tag tag, final JsonObject object) {
        if ((key.equals("contents")) && tag instanceof CompoundTag) {
            // Back to a UUID string
            final CompoundTag showEntity = (CompoundTag) tag;
            final Tag idTag = showEntity.get("id");
            if (idTag instanceof IntArrayTag) {
                showEntity.remove("id");

                final JsonObject convertedElement = (JsonObject) convertToJson(key, tag);
                final UUID uuid = fromIntArray(((IntArrayTag) idTag).getValue());
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
    private static String stringValue(Tag tag) {
        if (tag instanceof ByteArrayTag) {
            return Arrays.toString(((ByteArrayTag) tag).getValue());
        } else if (tag instanceof ByteTag) {
            return Byte.toString(((ByteTag) tag).getValue());
        } else if (tag instanceof DoubleTag) {
            return Double.toString(((DoubleTag) tag).getValue());
        } else if (tag instanceof FloatTag) {
            return Float.toString(((FloatTag) tag).getValue());
        } else if (tag instanceof IntArrayTag) {
            return Arrays.toString(((IntArrayTag) tag).getValue());
        } else if (tag instanceof IntTag) {
            return Integer.toString(((IntTag) tag).getValue());
        } else if (tag instanceof LongArrayTag) {
            return Arrays.toString(((LongArrayTag) tag).getValue());
        } else if (tag instanceof LongTag) {
            return Long.toString(((LongTag) tag).getValue());
        } else if (tag instanceof ShortTag) {
            return Short.toString(((ShortTag) tag).getValue());
        } else if (tag instanceof StringTag) {
            return ((StringTag) tag).getValue();
        } else {
            return tag.getValue().toString();
        }
    }

    // Implemented in the same way as ViaVersion's custom Pair class in order to reduce diff
    @AllArgsConstructor
    private static class Pair<K, V> {
        private final K key;
        private final V value;
    }
}
