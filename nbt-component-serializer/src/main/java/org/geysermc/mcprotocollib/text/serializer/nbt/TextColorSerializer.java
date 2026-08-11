package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextColor;
import org.cloudburstmc.nbt.NbtList;

/**
 * The two colour forms a style can carry.
 *
 * <p>They do not share a representation: vanilla's {@code TextColor} is a string that is either one
 * of the sixteen legacy names or a {@code #RRGGBB} literal, while a shadow colour is a packed ARGB
 * number and so keeps its alpha channel.
 */
final class TextColorSerializer {

    /**
     * The number of channels in the list form of an ARGB colour, and the scale each of them uses.
     * See {@code ExtraCodecs#ARGB_COLOR_CODEC}.
     */
    private static final int ARGB_CHANNELS = 4;
    private static final float ARGB_CHANNEL_SCALE = 255.0F;

    private TextColorSerializer() {
    }

    /**
     * Mirrors vanilla's {@code TextColor#parseColor}: a name wins over a hex literal, so a value that
     * is neither is an error rather than a silently dropped colour.
     */
    static TextColor deserialize(final String value) {
        final NamedTextColor named = NamedTextColor.NAMES.value(value);
        if (named != null) {
            return named;
        }

        final TextColor hex = TextColor.fromHexString(value);
        if (hex == null) {
            throw new NbtSerializationException("Not a valid colour name or #RRGGBB literal: '" + value + "'");
        }
        return hex;
    }

    /**
     * Writes the name of one of the sixteen legacy colours, and a hex literal for anything else, the
     * way vanilla's {@code TextColor#serialize} does.
     */
    static String serialize(final TextColor color) {
        if (color instanceof NamedTextColor named) {
            return NamedTextColor.NAMES.key(named);
        }
        return color.asHexString();
    }

    /**
     * Reads either form of {@code ExtraCodecs#ARGB_COLOR_CODEC}: the packed integer it writes, or the
     * list of four {@code 0..1} floats it also accepts so that colours can be authored channel by
     * channel in data packs.
     */
    static ShadowColor deserializeShadow(final Object tag) {
        if (tag instanceof Number number) {
            return ShadowColor.shadowColor(number.intValue());
        } else if (tag instanceof NbtList<?> channels) {
            if (channels.size() != ARGB_CHANNELS) {
                throw new NbtSerializationException("A shadow colour written as a list needs "
                    + ARGB_CHANNELS + " channels, got " + channels.size());
            }
            return ShadowColor.shadowColor(
                readChannel(channels.get(0)),
                readChannel(channels.get(1)),
                readChannel(channels.get(2)),
                readChannel(channels.get(3)));
        }
        throw new NbtSerializationException("Expected a shadow colour to be a number or a list, got " + Tags.typeNameOf(tag));
    }

    static int serializeShadow(final ShadowColor color) {
        return color.value();
    }

    private static int readChannel(final Object tag) {
        if (tag instanceof Number number) {
            return Math.round(number.floatValue() * ARGB_CHANNEL_SCALE);
        }
        throw new NbtSerializationException("Expected a shadow colour channel to be a number, got " + Tags.typeNameOf(tag));
    }
}
