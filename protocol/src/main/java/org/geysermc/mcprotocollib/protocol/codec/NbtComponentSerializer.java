package org.geysermc.mcprotocollib.protocol.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Converts between the nbt representation of a text component, as sent over the network, and its json
 * representation, as understood by adventure's gson serializer.
 *
 * <p>Originally taken from <a href="https://github.com/ViaVersion/ViaVersion/blob/4aefc23bb8074303c713a94d7f583ba4020dda04/common/src/main/java/com/viaversion/viaversion/protocols/protocol1_20_3to1_20_2/util/ComponentConverter.java">ViaVersion's ComponentConverter</a>,
 * since adjusted to match vanilla's {@code ComponentSerialization} and {@code NbtOps}.
 */
public class NbtComponentSerializer {

    /**
     * Vanilla stores heterogeneous nbt lists as a list of compounds, wrapping every entry that isn't
     * already a plain compound in a single-entry compound under this key. See {@code ListTag#write}
     * and {@code ListTag#addAndUnwrap}.
     */
    private static final String WRAPPER_MARKER = "";

    /**
     * Every {@code Codec.BOOL} field reachable from a component. Booleans have no direct nbt
     * representation, so they arrive as bytes and have to be turned back into json booleans.
     */
    private static final Set<String> BOOLEAN_TYPES = Set.of(
        // NbtContents
        "interpret",
        "plain",
        // PlayerSprite, of the object content type
        "hat",
        // Style
        "bold",
        "italic",
        "underlined",
        "strikethrough",
        "obfuscated"
    );

    /**
     * The component content types, along with the fields that identify them when no explicit
     * {@code type} discriminator is present.
     *
     * <p>Order is important: it must match the registration order in
     * {@code ComponentSerialization#bootstrap}, which is the order vanilla's {@code FuzzyCodec}
     * tries the content codecs in.
     */
    private static final List<ComponentType> COMPONENT_TYPES = List.of(
        new ComponentType("text", List.of("text")),
        new ComponentType("translatable", List.of("translate")),
        new ComponentType("keybind", List.of("keybind")),
        new ComponentType("score", List.of("score")),
        new ComponentType("selector", List.of("selector")),
        new ComponentType("nbt", List.of("nbt")),
        // ObjectContents has no field of its own; it inlines ObjectInfos, which is itself a legacy
        // component matcher keyed on "object", with atlas ("sprite") and player ("player") variants
        new ComponentType("object", List.of("object", "sprite", "player"))
    );

    private static final Set<String> COMPONENT_TYPE_NAMES = COMPONENT_TYPES.stream()
        .map(ComponentType::name)
        .collect(Collectors.toUnmodifiableSet());

    /**
     * Fields of a component whose contents are arbitrary nbt rather than more component data: a custom
     * click event's payload, and the data components of a shown item.
     *
     * <p>Everything below one of these is passed through untouched. The rest of this class works off
     * field names, and that data is free to use any name it likes - a payload holding
     * {@code {"type": "translatable", "bold": 1b}} means nothing to the component codec, and must not
     * be rewritten as if it did.
     */
    private static final Set<String> OPAQUE_FIELDS = Set.of(
        // ClickEvent.Custom
        "payload",
        // ItemStackTemplate, of a show_item hover event
        "components"
    );

    private NbtComponentSerializer() {

    }

    @Contract("null -> null")
    public static JsonElement tagComponentToJson(@Nullable final Object tag) {
        return convertToJson(null, tag, false);
    }

    public static @Nullable Object jsonComponentToTag(@Nullable final JsonElement component) {
        return convertToTag(component, false);
    }

    /**
     * @param opaque whether this sits inside one of the {@link #OPAQUE_FIELDS}, in which case the
     *               contents are passed through without any component specific interpretation
     */
    @Contract("null, _ -> null")
    private static Object convertToTag(final @Nullable JsonElement element, final boolean opaque) {
        if (element == null || element.isJsonNull()) {
            return null;
        } else if (element.isJsonObject()) {
            final NbtMapBuilder tag = NbtMap.builder();
            final JsonObject jsonObject = element.getAsJsonObject();
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                final String key = entry.getKey();
                tag.put(key, convertToTag(entry.getValue(), opaque || OPAQUE_FIELDS.contains(key)));
            }

            if (!opaque) {
                addComponentType(jsonObject, tag);
            }
            return tag.build();
        } else if (element.isJsonArray()) {
            return convertJsonArray(element.getAsJsonArray(), opaque);
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
            }
            return convertUntypedNumber(number);
        }
        throw new IllegalArgumentException("Unhandled json type " + element.getClass().getSimpleName() + " with value " + element.getAsString());
    }

    /**
     * Gson hands back a {@code LazilyParsedNumber} for every unqualified json number, so the original
     * java type is lost. Recover the narrowest tag type that holds the value without truncating it -
     * vanilla's {@code ExtraCodecs#JAVA} keeps a translation argument as whatever number it was.
     */
    private static Object convertUntypedNumber(final Number number) {
        final String value = number.toString();
        // Anything that isn't a plain integer literal (decimals, exponents, NaN, Infinity) can only
        // be represented as a double
        if (value.indexOf('.') == -1 && value.indexOf('e') == -1 && value.indexOf('E') == -1
            && value.indexOf('N') == -1 && value.indexOf('I') == -1) {
            try {
                return Integer.parseInt(value);
            } catch (final NumberFormatException ignored) {
            }

            try {
                return Long.parseLong(value);
            } catch (final NumberFormatException ignored) {
            }
        }

        return number.doubleValue();
    }

    private static NbtList<?> convertJsonArray(final JsonArray array, final boolean opaque) {
        NbtListBuilder<?> listBuilder = null;
        for (final JsonElement entry : array) {
            final Object convertedEntryTag = convertToTag(entry, opaque);
            final NbtType<?> convertedTagType = NbtType.byClass(convertedEntryTag.getClass());

            if (listBuilder == null) {
                listBuilder = new NbtListBuilder<>(convertedTagType);
            }

            // If we have different types mixed, fall back to the wrapped compound form below
            if (listBuilder.type != convertedTagType) {
                listBuilder = null;
                break;
            }

            listBuilder.addUnsafe(convertedEntryTag);
        }

        if (listBuilder != null) {
            return listBuilder.build();
        }

        if (array.isEmpty()) {
            // Vanilla's ListTag reports TAG_End as the element type of an empty list
            return new NbtList<>(NbtType.END, List.of());
        }

        // Mixed types. This is the normal case for a translatable component's "with" arguments,
        // which may hold any mix of numbers, booleans, strings and components, and vanilla stores
        // them as a list of compounds with every non-compound entry wrapped in {"": value}.
        final NbtListBuilder<NbtMap> wrappedList = new NbtListBuilder<>(NbtType.COMPOUND);
        for (final JsonElement entry : array) {
            wrappedList.add(wrapListEntry(convertToTag(entry, opaque)));
        }

        return wrappedList.build();
    }

    private static NbtMap wrapListEntry(final Object tag) {
        if (tag instanceof NbtMap map && !isWrappedListEntry(map)) {
            return map;
        }

        final NbtMapBuilder wrapper = NbtMap.builder();
        wrapper.put(WRAPPER_MARKER, tag);
        return wrapper.build();
    }

    private static Object unwrapListEntry(final Object tag) {
        if (tag instanceof NbtMap map && isWrappedListEntry(map)) {
            return map.get(WRAPPER_MARKER);
        }

        return tag;
    }

    private static boolean isWrappedListEntry(final NbtMap map) {
        return map.size() == 1 && map.containsKey(WRAPPER_MARKER);
    }

    private static void addComponentType(final JsonObject object, final NbtMapBuilder tag) {
        if (object.has("type")) {
            return;
        }

        // Add the type so vanilla's StrictEither takes the typed path instead of the FuzzyCodec,
        // which speeds up deserialization and makes DFU errors slightly more useful
        for (final ComponentType componentType : COMPONENT_TYPES) {
            for (final String field : componentType.fields()) {
                if (object.has(field)) {
                    tag.put("type", componentType.name());
                    return;
                }
            }
        }
    }

    /**
     * @param opaque whether this sits inside one of the {@link #OPAQUE_FIELDS}, in which case the
     *               contents are passed through without any component specific interpretation
     */
    private static @Nullable JsonElement convertToJson(final @Nullable String key, final @Nullable Object tag, final boolean opaque) {
        if (tag == null) {
            return null;
        } else if (tag instanceof NbtMap nbtMap) {
            final JsonObject object = new JsonObject();
            for (final Map.Entry<String, Object> entry : nbtMap.entrySet()) {
                final String entryKey = entry.getKey();
                object.add(entryKey, convertToJson(entryKey, entry.getValue(), opaque || OPAQUE_FIELDS.contains(entryKey)));
            }

            if (!opaque) {
                removeComponentType(object);
            }
            return object;
        } else if (tag instanceof NbtList<?> list) {
            final JsonArray array = new JsonArray();
            for (final Object listEntry : list) {
                array.add(convertToJson(null, unwrapListEntry(listEntry), opaque));
            }
            return array;
        } else if (tag instanceof Number number) {
            if (!opaque && key != null && BOOLEAN_TYPES.contains(key)) {
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

    /**
     * Strips the {@code type} discriminator and the fields belonging to the other content types.
     *
     * <p>Vanilla itself never writes {@code type} - its {@code StrictEither} only reads it - but other
     * senders do, and adventure ignores it entirely, picking the content type purely by field
     * presence. Without this, {@code {"type": "translatable", "text": "a", "translate": "b"}} would be
     * read as translatable by vanilla but as text by adventure.
     */
    private static void removeComponentType(final JsonObject object) {
        final JsonElement type = object.get("type");
        if (type == null || !type.isJsonPrimitive() || !type.getAsJsonPrimitive().isString()) {
            return;
        }

        final String typeString = type.getAsString();
        // Arbitrary nbt travels inside components (item components, custom click event payloads) and
        // may carry a "type" field that has nothing to do with component content types
        if (!COMPONENT_TYPE_NAMES.contains(typeString)) {
            return;
        }

        object.remove("type");

        // Remove the other content types' fields
        for (final ComponentType componentType : COMPONENT_TYPES) {
            if (!componentType.name().equals(typeString)) {
                componentType.fields().forEach(object::remove);
            }
        }
    }

    private record ComponentType(String name, List<String> fields) {
    }

    @RequiredArgsConstructor
    private static class NbtListBuilder<T> {
        private final NbtType<T> type;
        private final List<T> list = new ArrayList<>();

        public void add(T value) {
            list.add(value);
        }

        public void addUnsafe(Object value) {
            add(type.getTagClass().cast(value));
        }

        public NbtList<T> build() {
            return new NbtList<>(type, list);
        }
    }
}
