package com.github.steveice10.mc.auth.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

/**
 * Utility class for serializing and deserializing UUIDs.
 */
public class UUIDSerializer extends TypeAdapter<UUID> {
    /**
     * Converts a UUID to a String.
     *
     * @param value UUID to convert.
     * @return The resulting String.
     */
    public static String fromUUID(UUID value) {
        if (value == null) {
            return "";
        }

        return value.toString().replace("-", "");
    }

    /**
     * Converts a String to a UUID.
     *
     * @param value String to convert.
     * @return The resulting UUID.
     */
    public static UUID fromString(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        return UUID.fromString(value.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }

    @Override
    public void write(JsonWriter out, UUID value) throws IOException {
        out.value(fromUUID(value));
    }

    @Override
    public UUID read(JsonReader in) throws IOException {
        return fromString(in.nextString());
    }
}
