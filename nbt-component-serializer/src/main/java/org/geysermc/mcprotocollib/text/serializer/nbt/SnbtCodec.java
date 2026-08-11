package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.util.Codec;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Reads and writes tags in vanilla's stringified nbt dialect, as produced by {@code SnbtPrinterTagVisitor}
 * and consumed by {@code TagParser}.
 *
 * <p>Adventure can only carry opaque nbt as a string: both {@code ClickEvent.Payload.Custom} and
 * {@code HoverEvent.ShowItem} hold a {@link net.kyori.adventure.nbt.api.BinaryTagHolder}, which is a
 * string plus a {@link Codec} to turn it back into whatever tag model the caller uses. {@link #CODEC}
 * is that codec for cloudburst tags, so a holder built from it round trips losslessly while still
 * reading as ordinary snbt to anything else that looks at the string.
 */
public final class SnbtCodec {

    /**
     * The codec to hand to {@link net.kyori.adventure.nbt.api.BinaryTagHolder#encode(Object, Codec)}
     * and {@link net.kyori.adventure.nbt.api.BinaryTagHolder#get(Codec)}.
     */
    public static final Codec<Object, String, NbtSerializationException, NbtSerializationException> CODEC =
        Codec.codec(SnbtCodec::fromSnbt, SnbtCodec::toSnbt);

    /**
     * The characters vanilla's {@code StringReader#isAllowedInUnquotedString} accepts, and so the keys
     * and strings that can be written without quotes.
     */
    private static final Pattern UNQUOTED = Pattern.compile("[A-Za-z0-9_\\-.+]+");

    private static final Pattern BYTE = Pattern.compile("[-+]?\\d+[bB]");
    private static final Pattern SHORT = Pattern.compile("[-+]?\\d+[sS]");
    private static final Pattern INT = Pattern.compile("[-+]?\\d+");
    private static final Pattern LONG = Pattern.compile("[-+]?\\d+[lL]");
    private static final Pattern FLOAT = Pattern.compile("[-+]?(?:\\d+\\.?\\d*|\\.\\d+)(?:[eE][-+]?\\d+)?[fF]");
    private static final Pattern DOUBLE = Pattern.compile("[-+]?(?:\\d+\\.?\\d*|\\.\\d+)(?:[eE][-+]?\\d+)?[dD]");

    /**
     * Vanilla treats a decimal number with no type suffix as a double, so {@code 1.5} and {@code 1.5d}
     * read the same. Only a bare integer defaults to a different type.
     */
    private static final Pattern IMPLICIT_DOUBLE = Pattern.compile("[-+]?(?:\\d+\\.\\d*|\\.\\d+|\\d+(?:\\.\\d*)?[eE][-+]?\\d+)");

    private SnbtCodec() {
    }

    /**
     * Writes a cloudburst tag - a {@link String}, a boxed number, a primitive array, an {@link NbtMap}
     * or an {@link NbtList} - as snbt.
     */
    public static String toSnbt(final Object tag) {
        final StringBuilder builder = new StringBuilder();
        write(builder, tag);
        return builder.toString();
    }

    /**
     * Reads snbt back into a cloudburst tag.
     *
     * @throws NbtSerializationException if the string is not well formed snbt
     */
    public static Object fromSnbt(final String snbt) {
        final SnbtReader reader = new SnbtReader(snbt);
        final Object tag = reader.readValue();
        reader.skipWhitespace();
        if (!reader.atEnd()) {
            throw reader.error("Trailing data after the tag");
        }
        return tag;
    }

    // Writing

    private static void write(final StringBuilder builder, final Object tag) {
        switch (tag) {
            case String string -> writeQuoted(builder, string);
            case Byte value -> builder.append(value.byteValue()).append('b');
            case Short value -> builder.append(value.shortValue()).append('s');
            case Integer value -> builder.append(value.intValue());
            case Long value -> builder.append(value.longValue()).append('L');
            case Float value -> builder.append(value.floatValue()).append('f');
            case Double value ->
                // The suffix is what keeps a double a double on the way back; without it vanilla would
                // still read a double, but only because a decimal point is present
                builder.append(value.doubleValue()).append('d');
            case byte[] values -> writeByteArray(builder, values);
            case int[] values -> writeIntArray(builder, values);
            case long[] values -> writeLongArray(builder, values);
            case NbtMap compound -> writeCompound(builder, compound);
            case NbtList<?> list -> writeList(builder, list);
            case null, default -> throw new NbtSerializationException("Not a valid nbt value: " + Tags.typeNameOf(tag));
        }
    }

    private static void writeCompound(final StringBuilder builder, final NbtMap compound) {
        builder.append('{');
        boolean first = true;
        for (final Map.Entry<String, Object> entry : compound.entrySet()) {
            if (!first) {
                builder.append(',');
            }
            first = false;
            writeKey(builder, entry.getKey());
            builder.append(':');
            write(builder, entry.getValue());
        }
        builder.append('}');
    }

    private static void writeList(final StringBuilder builder, final NbtList<?> list) {
        builder.append('[');
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                builder.append(',');
            }
            write(builder, list.get(i));
        }
        builder.append(']');
    }

    private static void writeByteArray(final StringBuilder builder, final byte[] values) {
        builder.append("[B;");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(values[i]).append('b');
        }
        builder.append(']');
    }

    private static void writeIntArray(final StringBuilder builder, final int[] values) {
        builder.append("[I;");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(values[i]);
        }
        builder.append(']');
    }

    private static void writeLongArray(final StringBuilder builder, final long[] values) {
        builder.append("[L;");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(values[i]).append('L');
        }
        builder.append(']');
    }

    private static void writeKey(final StringBuilder builder, final String key) {
        if (UNQUOTED.matcher(key).matches()) {
            builder.append(key);
        } else {
            writeQuoted(builder, key);
        }
    }

    /**
     * Values are always quoted, even when they would survive unquoted, so that a string that happens to
     * look like a number keeps its type.
     */
    private static void writeQuoted(final StringBuilder builder, final String value) {
        builder.append('"');
        for (int i = 0; i < value.length(); i++) {
            final char character = value.charAt(i);
            if (character == '"' || character == '\\') {
                builder.append('\\');
            }
            builder.append(character);
        }
        builder.append('"');
    }

    // Reading

    /**
     * A cursor over the string, mirroring what vanilla's {@code TagParser} does on top of
     * {@code StringReader}: every read consumes from the current position, and any failure reports
     * where it happened.
     */
    private static final class SnbtReader {

        private final String input;
        private int cursor;

        private SnbtReader(final String input) {
            this.input = input;
        }

        private Object readValue() {
            this.skipWhitespace();
            if (this.atEnd()) {
                throw this.error("Expected a value");
            }

            return switch (this.peek()) {
                case '{' -> this.readCompound();
                case '[' -> this.readListOrArray();
                case '"', '\'' -> this.readQuotedString();
                default -> this.readUnquotedValue();
            };
        }

        private NbtMap readCompound() {
            this.expect('{');
            final NbtMapBuilder builder = NbtMap.builder();

            this.skipWhitespace();
            if (this.tryConsume('}')) {
                return builder.build();
            }

            while (true) {
                this.skipWhitespace();
                final String key = this.readKey();
                this.skipWhitespace();
                this.expect(':');
                builder.put(key, this.readValue());

                this.skipWhitespace();
                if (this.tryConsume(',')) {
                    continue;
                }
                this.expect('}');
                return builder.build();
            }
        }

        private String readKey() {
            if (this.atEnd()) {
                throw this.error("Expected a key");
            }

            final char character = this.peek();
            if (character == '"' || character == '\'') {
                return this.readQuotedString();
            }

            final String key = this.readUnquotedToken();
            if (key.isEmpty()) {
                throw this.error("Expected a key");
            }
            return key;
        }

        private Object readListOrArray() {
            final int start = this.cursor;
            this.expect('[');

            // [B;, [I; and [L; are the only prefixes vanilla gives a list, and only immediately after
            // the bracket - anything else is an ordinary list whose first element starts here
            if (this.cursor + 1 < this.input.length() && this.input.charAt(this.cursor + 1) == ';') {
                final char marker = this.peek();
                if (marker == 'B' || marker == 'I' || marker == 'L') {
                    this.cursor += 2;
                    return this.readArray(marker, start);
                }
            }

            return this.readList(start);
        }

        private NbtList<?> readList(final int start) {
            this.skipWhitespace();
            if (this.tryConsume(']')) {
                return new NbtList<>(NbtType.END, List.of());
            }

            final List<Object> entries = new ArrayList<>();
            NbtType<?> sharedType = null;
            while (true) {
                final Object entry = this.readValue();
                final NbtType<?> entryType = Tags.typeOf(entry);
                if (sharedType == null) {
                    sharedType = entryType;
                } else if (sharedType != entryType) {
                    // A cloudburst list carries one element type for the whole list, and snbt has no way
                    // to say which entries were wrapped, so a mixed list cannot be represented
                    throw new NbtSerializationException("Mixed element types in the list at position " + start
                        + ": " + sharedType.getTypeName() + " and " + entryType.getTypeName());
                }
                entries.add(entry);

                this.skipWhitespace();
                if (this.tryConsume(',')) {
                    continue;
                }
                this.expect(']');
                return Tags.buildList(entries);
            }
        }

        private Object readArray(final char marker, final int start) {
            this.skipWhitespace();
            final List<Number> values = new ArrayList<>();
            if (!this.tryConsume(']')) {
                while (true) {
                    final int valueStart = this.cursor;
                    final Object value = this.readValue();
                    if (!(value instanceof Number number)) {
                        throw new NbtSerializationException("Expected a number in the array at position "
                            + valueStart + ", got " + Tags.typeNameOf(value));
                    }
                    values.add(number);

                    this.skipWhitespace();
                    if (this.tryConsume(',')) {
                        this.skipWhitespace();
                        continue;
                    }
                    this.expect(']');
                    break;
                }
            }

            return switch (marker) {
                case 'B' -> toByteArray(values);
                case 'I' -> toIntArray(values);
                default -> toLongArray(values);
            };
        }

        private String readQuotedString() {
            final char quote = this.next();
            final StringBuilder builder = new StringBuilder();
            while (true) {
                if (this.atEnd()) {
                    throw this.error("Unterminated string");
                }

                final char character = this.next();
                if (character == quote) {
                    return builder.toString();
                } else if (character != '\\') {
                    builder.append(character);
                    continue;
                }

                if (this.atEnd()) {
                    throw this.error("Unterminated escape sequence");
                }
                final char escaped = this.next();
                if (escaped != '\\' && escaped != '"' && escaped != '\'') {
                    throw new NbtSerializationException("Invalid escape sequence '\\" + escaped
                        + "' at position " + (this.cursor - 1));
                }
                builder.append(escaped);
            }
        }

        /**
         * Reads a bare token as the narrowest thing it can be: a number when it carries a type suffix or
         * looks like one, a boolean as vanilla's byte, and otherwise the string itself.
         */
        private Object readUnquotedValue() {
            final int start = this.cursor;
            final String token = this.readUnquotedToken();
            if (token.isEmpty()) {
                throw new NbtSerializationException("Expected a value at position " + start
                    + ", got '" + this.peek() + "'");
            }

            try {
                if (BYTE.matcher(token).matches()) {
                    return Byte.valueOf(stripSuffix(token));
                } else if (SHORT.matcher(token).matches()) {
                    return Short.valueOf(stripSuffix(token));
                } else if (LONG.matcher(token).matches()) {
                    return Long.valueOf(stripSuffix(token));
                } else if (INT.matcher(token).matches()) {
                    return Integer.valueOf(token);
                } else if (FLOAT.matcher(token).matches()) {
                    return Float.valueOf(stripSuffix(token));
                } else if (DOUBLE.matcher(token).matches()) {
                    return Double.valueOf(stripSuffix(token));
                } else if (IMPLICIT_DOUBLE.matcher(token).matches()) {
                    return Double.valueOf(token);
                }
            } catch (final NumberFormatException exception) {
                throw new NbtSerializationException("Number '" + token + "' at position " + start
                    + " is out of range", exception);
            }

            if (token.equals("true")) {
                return Tags.writeBoolean(true);
            } else if (token.equals("false")) {
                return Tags.writeBoolean(false);
            }
            return token;
        }

        private String readUnquotedToken() {
            final int start = this.cursor;
            while (!this.atEnd() && isUnquotedCharacter(this.peek())) {
                this.cursor++;
            }
            return this.input.substring(start, this.cursor);
        }

        private void skipWhitespace() {
            while (!this.atEnd() && Character.isWhitespace(this.peek())) {
                this.cursor++;
            }
        }

        private boolean atEnd() {
            return this.cursor >= this.input.length();
        }

        private char peek() {
            return this.input.charAt(this.cursor);
        }

        private char next() {
            return this.input.charAt(this.cursor++);
        }

        private boolean tryConsume(final char expected) {
            if (!this.atEnd() && this.peek() == expected) {
                this.cursor++;
                return true;
            }
            return false;
        }

        private void expect(final char expected) {
            this.skipWhitespace();
            if (this.atEnd()) {
                throw new NbtSerializationException("Expected '" + expected + "' at position " + this.cursor
                    + ", but the tag ended");
            } else if (this.peek() != expected) {
                throw new NbtSerializationException("Expected '" + expected + "' at position " + this.cursor
                    + ", got '" + this.peek() + "'");
            }
            this.cursor++;
        }

        private NbtSerializationException error(final String message) {
            return new NbtSerializationException(message + " at position " + this.cursor);
        }
    }

    private static boolean isUnquotedCharacter(final char character) {
        return (character >= 'a' && character <= 'z')
            || (character >= 'A' && character <= 'Z')
            || (character >= '0' && character <= '9')
            || character == '_' || character == '-' || character == '.' || character == '+';
    }

    private static String stripSuffix(final String token) {
        return token.substring(0, token.length() - 1);
    }

    private static byte[] toByteArray(final List<Number> values) {
        final byte[] array = new byte[values.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = values.get(i).byteValue();
        }
        return array;
    }

    private static int[] toIntArray(final List<Number> values) {
        final int[] array = new int[values.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = values.get(i).intValue();
        }
        return array;
    }

    private static long[] toLongArray(final List<Number> values) {
        final long[] array = new long[values.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = values.get(i).longValue();
        }
        return array;
    }
}
