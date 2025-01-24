package org.geysermc.mcprotocollib.auth.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

/**
 * Utility class for serializing and deserializing undashed UUIDs.
 */
public class UndashedUUIDAdapter extends TypeAdapter<UUID> {
    @Override
    public void write(JsonWriter out, UUID value) throws IOException {
        out.value(UUIDUtils.convertToNoDashes(value));
    }

    @Override
    public UUID read(JsonReader in) throws IOException {
        return UUIDUtils.convertToDashed(in.nextString());
    }
}
