package org.geysermc.mcprotocollib.protocol.data;

import lombok.Setter;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.geysermc.adventure.text.serializer.nbt.NbtComponentSerializer;

public final class DefaultComponentSerializer {
    private static GsonComponentSerializer gson = GsonComponentSerializer.gson();
    @Setter
    private static NbtComponentSerializer nbt = NbtComponentSerializer.nbt();

    public static GsonComponentSerializer get() {
        return gson;
    }

    public static void set(GsonComponentSerializer serializer) {
        gson = serializer;
    }

    public static NbtComponentSerializer nbt() {
        return nbt;
    }

    private DefaultComponentSerializer() {}
}
