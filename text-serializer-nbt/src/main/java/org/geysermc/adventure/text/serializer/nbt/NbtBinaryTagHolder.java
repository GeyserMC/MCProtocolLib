package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.util.Codec;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtUtils;
import org.jspecify.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

public final class NbtBinaryTagHolder implements BinaryTagHolder {
    public static final Codec<Object, String, RuntimeException, RuntimeException> NBT_CODEC = Codec.codec(NbtBinaryTagHolder::decodeNbtFromBase64, NbtBinaryTagHolder::encodeNbtToBase64);
    private final Object tag;
    private @Nullable String string;

    public NbtBinaryTagHolder(Object tag) {
        this.tag = tag;
    }

    @Override
    public String string() {
        if (string == null) {
            string = NBT_CODEC.encode(tag);
        }
        return string;
    }

    @Override
    public <T, DX extends Exception> T get(Codec<T, String, DX, ?> codec) throws DX {
        return codec.decode(string());
    }

    public Object tag() {
        return tag;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof BinaryTagHolder otherTag) {
            if (otherTag instanceof NbtBinaryTagHolder nbtTag) {
                // Don't compare string field, it'll always be the same (when initialised)
                return Objects.equals(this.tag, nbtTag.tag);
            }
            // This'll probably fail due to our codec working differently and not encoding to SNBT
            return Objects.equals(string(), otherTag.string());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }

    @Override
    public String toString() {
        return tag.toString();
    }

    private static String encodeNbtToBase64(Object object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NBTOutputStream nbtOutput = NbtUtils.createWriter(outputStream);
        try {
            nbtOutput.writeTag(object);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to encode NBT tag to Base64 string", exception);
        }
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    private static Object decodeNbtFromBase64(String string) {
        byte[] bytes = Base64.getDecoder().decode(string);
        try {
            return NbtUtils.createReader(new ByteArrayInputStream(bytes)).readTag();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to decode NBT tag from Base64 string", exception);
        }
    }
}
