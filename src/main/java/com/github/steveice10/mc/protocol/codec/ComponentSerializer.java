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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Taken from https://github.com/ViaVersion/ViaVersion/blob/dev/common/src/main/java/com/viaversion/viaversion/protocols/protocol1_20_3to1_20_2/Protocol1_20_3To1_20_2.java
 */
public class ComponentSerializer {

    private static final Set<String> BOOLEAN_TYPES = new HashSet<>(Arrays.asList(
        "interpret",
        "bold",
        "italic",
        "underlined",
        "strikethrough",
        "obfuscated"
    ));

    private ComponentSerializer() {

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
            for (final Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                // Not strictly needed, but might as well make it more compact
                convertObjectEntry(entry.getKey(), entry.getValue(), tag);
            }
            return tag;
        } else if (element.isJsonArray()) {
            return convertJsonArray(name, element);
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
            }
            return new StringTag(primitive.getAsString()); // ???
        }
        throw new IllegalArgumentException("Unhandled json type " + element.getClass().getSimpleName() + " with value " + element.getAsString());
    }

    private static ListTag convertJsonArray(String name, final JsonElement element) {
        // TODO Number arrays?
        final ListTag listTag = new ListTag(name);
        boolean singleType = true;
        for (final JsonElement entry : element.getAsJsonArray()) {
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
        for (final JsonElement entry : element.getAsJsonArray()) {
            final Tag convertedTag = convertToTag("extra", entry);
            if (convertedTag instanceof CompoundTag) {
                processedListTag.add(listTag);
                continue;
            }

            // Wrap all entries in compound tags as lists can only consist of one type of tag
            final CompoundTag compoundTag = new CompoundTag("");
            compoundTag.put(new StringTag("text"));
            compoundTag.put(convertedTag);
        }
        return processedListTag;
    }

    private static void convertObjectEntry(final String key, final JsonElement element, final CompoundTag tag) {
        if ((key.equals("contents")) && element.isJsonObject()) {
            // Store show_entity id as int array instead of uuid string
            final JsonObject hoverEvent = element.getAsJsonObject();
            final JsonElement id = hoverEvent.get("id");
            final UUID uuid;
            if (id != null && id.isJsonPrimitive() && (uuid = parseUUID(id.getAsString())) != null) {
                hoverEvent.remove("id");

                final CompoundTag convertedTag = (CompoundTag) convertToTag(key, element);
                convertedTag.put(new IntArrayTag("id", uuidToIntArray(uuid)));
                tag.put(convertedTag);
                return;
            }
        }

        tag.put(convertToTag(key, element));
    }

    private static @Nullable UUID parseUUID(final String uuidString) {
        try {
            return UUID.fromString(uuidString);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    @Contract("_, null -> null")
    private static JsonElement convertToJson(final @Nullable String key, final @Nullable Tag tag) {
        if (tag == null) {
            return null;
        } else if (tag instanceof CompoundTag) {
            final JsonObject object = new JsonObject();
            for (final Tag entry : (CompoundTag) tag) {
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
            Number number = (Number) tag.getValue();
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
                convertedElement.addProperty("id", uuidIntsToString(((IntArrayTag) idTag).getValue()));
                object.add(key, convertedElement);
                return;
            }
        }

        // "":1 is a valid tag, but not a valid json component
        object.add(key.isEmpty() ? "text" : key, convertToJson(key, tag));
    }

    private static String uuidIntsToString(final int[] parts) {
        if (parts.length != 4) {
            return new UUID(0, 0).toString();
        }
        return uuidFromIntArray(parts).toString();
    }

    private static UUID uuidFromIntArray(int[] ints) {
        return new UUID((long) ints[0] << 32 | ((long) ints[1] & 0xFFFFFFFFL), (long) ints[2] << 32 | ((long) ints[3] & 0xFFFFFFFFL));
    }

    private static int[] uuidToIntArray(UUID uuid) {
        return bitsToIntArray(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
    }

    private static int[] bitsToIntArray(long long1, long long2) {
        return new int[]{(int) (long1 >> 32), (int) long1, (int) (long2 >> 32), (int) long2};
    }
}
