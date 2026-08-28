package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.util.Codec;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public record CloudburstDataComponentValue(Object value) implements DataComponentValue.TagSerializable {
    public static final Codec<Object, String, RuntimeException, RuntimeException> NBT_CODEC = Codec.codec(CloudburstDataComponentValue::decodeNbtFromBase64, CloudburstDataComponentValue::encodeNbtToBase64);

    @Override
    public BinaryTagHolder asBinaryTag() {
        return BinaryTagHolder.encode(value, NBT_CODEC);
    }

    private static String encodeNbtToBase64(Object object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NBTOutputStream nbtOutput = NbtUtils.createWriter(outputStream);
        try {
            nbtOutput.writeTag(object);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    private static Object decodeNbtFromBase64(String string) {
        byte[] bytes = Base64.getDecoder().decode(string);
        try {
            return NbtUtils.createReader(new ByteArrayInputStream(bytes)).readTag();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
