package org.geysermc.mcprotocollib.text.serializer.nbt;

import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Helpers for the parts of nbt that don't line up one-to-one with java values.
 *
 * <p>Cloudburst represents a tag as a plain {@link Object} - a {@link String}, a boxed number, a
 * primitive array, an {@link NbtMap} or an {@link NbtList} - so there is no common tag supertype to
 * hang these off. They live here instead.
 */
final class Tags {

    /**
     * Vanilla writes a heterogeneous list as a list of compounds, wrapping every entry that isn't
     * already a plain compound in a single-entry compound under this key. See {@code ListTag#write}
     * and {@code ListTag#addAndUnwrap}.
     *
     * <p>Cloudburst's reader and writer don't do this for us: an {@link NbtList} carries a single
     * element type for the whole list, so a mixed list has to be built and taken apart by hand.
     */
    private static final String WRAPPER_MARKER = "";

    private Tags() {
    }

    /**
     * Nbt has no boolean type; vanilla's {@code NbtOps#createBoolean} writes a byte and
     * {@code getBooleanValue} treats any non-zero number as true.
     */
    static byte writeBoolean(final boolean value) {
        return (byte) (value ? 1 : 0);
    }

    static boolean readBoolean(final Object tag) {
        if (tag instanceof Number number) {
            return number.byteValue() != 0;
        }
        throw new NbtSerializationException("Expected a number to read as a boolean, got " + typeNameOf(tag));
    }

    /**
     * Reads an optional boolean field, tolerating a wrong type the way vanilla's
     * {@code lenientOptionalFieldOf} does for {@code interpret} and {@code plain}.
     */
    static boolean getBooleanOrDefault(final NbtMap tag, final String key, final boolean fallback) {
        final Object value = tag.get(key);
        return value instanceof Number number ? number.byteValue() != 0 : fallback;
    }

    static @Nullable String getString(final NbtMap tag, final String key) {
        final Object value = tag.get(key);
        if (value == null) {
            return null;
        } else if (value instanceof String string) {
            return string;
        }
        throw new NbtSerializationException("Expected field '" + key + "' to be a string, got " + typeNameOf(value));
    }

    static String getRequiredString(final NbtMap tag, final String key) {
        final String value = getString(tag, key);
        if (value == null) {
            throw new NbtSerializationException("Missing required string field '" + key + "'");
        }
        return value;
    }

    static @Nullable NbtMap getCompound(final NbtMap tag, final String key) {
        final Object value = tag.get(key);
        if (value == null) {
            return null;
        } else if (value instanceof NbtMap compound) {
            return compound;
        }
        throw new NbtSerializationException("Expected field '" + key + "' to be a compound, got " + typeNameOf(value));
    }

    static NbtMap getRequiredCompound(final NbtMap tag, final String key) {
        final NbtMap value = getCompound(tag, key);
        if (value == null) {
            throw new NbtSerializationException("Missing required compound field '" + key + "'");
        }
        return value;
    }

    static @Nullable Number getNumber(final NbtMap tag, final String key) {
        final Object value = tag.get(key);
        if (value == null) {
            return null;
        } else if (value instanceof Number number) {
            return number;
        }
        throw new NbtSerializationException("Expected field '" + key + "' to be a number, got " + typeNameOf(value));
    }

    /**
     * Reads a list field, unwrapping any {@link #WRAPPER_MARKER} entries so callers see the values
     * vanilla would have seen.
     */
    static List<Object> getList(final NbtMap tag, final String key) {
        final Object value = tag.get(key);
        if (value == null) {
            return List.of();
        }
        if (!(value instanceof NbtList<?> list)) {
            throw new NbtSerializationException("Expected field '" + key + "' to be a list, got " + typeNameOf(value));
        }

        final List<Object> entries = new ArrayList<>(list.size());
        for (final Object entry : list) {
            entries.add(unwrapListEntry(entry));
        }
        return entries;
    }

    /**
     * Builds the narrowest list that holds every entry: a homogeneous list when the entries share a
     * tag type, and vanilla's wrapped compound form when they don't.
     */
    static NbtList<?> buildList(final List<?> entries) {
        if (entries.isEmpty()) {
            // Vanilla's ListTag reports TAG_End as the element type of an empty list
            return new NbtList<>(NbtType.END, List.of());
        }

        NbtType<?> sharedType = null;
        for (final Object entry : entries) {
            final NbtType<?> entryType = typeOf(entry);
            if (sharedType == null) {
                sharedType = entryType;
            } else if (sharedType != entryType) {
                sharedType = null;
                break;
            }
        }

        if (sharedType != null) {
            return buildHomogeneousList(sharedType, entries);
        }

        final List<NbtMap> wrapped = new ArrayList<>(entries.size());
        for (final Object entry : entries) {
            wrapped.add(wrapListEntry(entry));
        }
        return new NbtList<>(NbtType.COMPOUND, wrapped);
    }

    @SuppressWarnings("unchecked")
    private static <T> NbtList<T> buildHomogeneousList(final NbtType<T> type, final List<?> entries) {
        final List<T> values = new ArrayList<>(entries.size());
        for (final Object entry : entries) {
            values.add((T) entry);
        }
        return new NbtList<>(type, values);
    }

    private static NbtMap wrapListEntry(final Object tag) {
        // A plain compound is already its own wrapper; only a compound that would be mistaken for a
        // wrapper on the way back needs wrapping itself
        if (tag instanceof NbtMap map && !isWrappedListEntry(map)) {
            return map;
        }
        // NbtMapBuilder#put follows Map semantics and returns the previous value, so it cannot be chained
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

    static NbtType<?> typeOf(final Object tag) {
        try {
            return NbtType.byClass(tag.getClass());
        } catch (final IllegalArgumentException e) {
            // byClass throws rather than returning null for a class that isn't tag representable
            throw new NbtSerializationException("Not a valid nbt value: " + tag.getClass().getName(), e);
        }
    }

    static String typeNameOf(final @Nullable Object tag) {
        return tag == null ? "null" : tag.getClass().getSimpleName();
    }
}
