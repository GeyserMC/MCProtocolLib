package org.geysermc.mcprotocollib.text.serializer.nbt;

import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.NBT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Everything that is not a component has to come back as an {@link NbtSerializationException} rather
 * than a class cast, a null, or a silently empty component.
 */
class MalformedInputTest {

    private static String messageFrom(final Object tag) {
        return assertThrows(NbtSerializationException.class, () -> NBT.deserialize(tag)).getMessage();
    }

    @Nested
    class MissingContents {

        @Test
        @DisplayName("a compound with only style fields is not a component")
        void noContentFields() {
            assertEquals("A component compound has no content fields: [color]",
                messageFrom(NbtMap.builder().putString("color", "red").build()));
        }

        @Test
        void emptyCompound() {
            assertEquals("A component compound has no content fields: []", messageFrom(NbtMap.EMPTY));
        }
    }

    @Nested
    class MissingRequiredFields {

        @Test
        @DisplayName("type says text but there is no text field")
        void textWithoutContent() {
            assertEquals("Missing required string field 'text'",
                messageFrom(NbtMap.builder().putString("type", "text").build()));
        }

        @Test
        void translatableWithoutKey() {
            assertEquals("Missing required string field 'translate'",
                messageFrom(NbtMap.builder().putString("type", "translatable").build()));
        }

        @Test
        void keybindWithoutKey() {
            assertEquals("Missing required string field 'keybind'",
                messageFrom(NbtMap.builder().putString("type", "keybind").build()));
        }

        @Test
        void scoreWithoutSubCompound() {
            assertEquals("Missing required compound field 'score'",
                messageFrom(NbtMap.builder().putString("type", "score").build()));
        }

        @Test
        void scoreWithoutObjective() {
            assertEquals("Missing required string field 'objective'",
                messageFrom(NbtMap.builder()
                    .putCompound("score", NbtMap.builder().putString("name", "Steve").build())
                    .build()));
        }
    }

    @Nested
    class WrongTypedFields {

        @Test
        @DisplayName("text as an int is not a string")
        void textAsAnInt() {
            assertEquals("Expected field 'text' to be a string, got Integer",
                messageFrom(NbtMap.builder().putInt("text", 5).build()));
        }

        @Test
        void scoreAsAString() {
            assertEquals("Expected field 'score' to be a compound, got String",
                messageFrom(NbtMap.builder().putString("score", "Steve deaths").build()));
        }

        @Test
        void extraAsAString() {
            assertEquals("Expected field 'extra' to be a list, got String",
                messageFrom(NbtMap.builder().putString("text", "a").putString("extra", "b").build()));
        }
    }

    @Nested
    class WrongTopLevelTag {

        @Test
        @DisplayName("a component must be a string, a list or a compound")
        void numberIsNotAComponent() {
            assertEquals("A component must be a string, a list or a compound, got Integer", messageFrom(42));
        }

        @Test
        void byteIsNotAComponent() {
            assertEquals("A component must be a string, a list or a compound, got Byte", messageFrom((byte) 1));
        }

        @Test
        void intArrayIsNotAComponent() {
            assertEquals("A component must be a string, a list or a compound, got int[]", messageFrom(new int[]{1, 2}));
        }
    }

    @Nested
    class BadListShorthand {

        @Test
        @DisplayName("an empty list has no first entry to be the component")
        void emptyList() {
            assertEquals("A component written as a list must not be empty",
                messageFrom(new NbtList<>(NbtType.END, List.of())));
        }

        @Test
        void emptyCompoundList() {
            assertEquals("A component written as a list must not be empty",
                messageFrom(new NbtList<>(NbtType.COMPOUND, List.of())));
        }

        @Test
        @DisplayName("a bad entry anywhere in the list fails the whole list")
        void badEntryInAList() {
            assertEquals("A component compound has no content fields: [color]",
                messageFrom(new NbtList<>(NbtType.COMPOUND, List.of(
                    NbtMap.builder().putString("text", "a").build(),
                    NbtMap.builder().putString("color", "red").build()))));
        }
    }
}
