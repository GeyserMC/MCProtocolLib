package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.DataComponentValue;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * A data component of a {@code show_item} hover event, holding the value as the cloudburst tag it was
 * read from.
 *
 * <p>Vanilla carries these components as nbt from end to end, and so does this serializer: a hover event
 * built out of these values never turns a tag into a string, and never has to parse one back. Only a
 * consumer that asks for the adventure {@link BinaryTagHolder} - some other serializer, or code written
 * against a different nbt model - pays for a conversion, and then only for the component it asked about.
 */
public final class NbtDataComponentValue implements DataComponentValue.TagSerializable {

    private final Object tag;

    private NbtDataComponentValue(final Object tag) {
        this.tag = tag;
    }

    public static NbtDataComponentValue nbtDataComponentValue(final Object tag) {
        return new NbtDataComponentValue(Objects.requireNonNull(tag, "tag"));
    }

    /**
     * The value as a cloudburst tag: a {@link String}, a boxed number, a primitive array, an
     * {@link org.cloudburstmc.nbt.NbtMap} or an {@link org.cloudburstmc.nbt.NbtList}.
     */
    public Object tag() {
        return this.tag;
    }

    @Override
    public @NonNull BinaryTagHolder asBinaryTag() {
        return BinaryTagHolder.encode(this.tag, SnbtCodec.CODEC);
    }

    /**
     * Compared deeply, because a tag may be a primitive array, which does not carry a useful identity of
     * its own.
     */
    @Override
    public boolean equals(final @Nullable Object other) {
        return this == other || (other instanceof NbtDataComponentValue value && Objects.deepEquals(this.tag, value.tag));
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[]{this.tag});
    }

    @Override
    public String toString() {
        return "NbtDataComponentValue{tag=" + this.tag + "}";
    }
}
