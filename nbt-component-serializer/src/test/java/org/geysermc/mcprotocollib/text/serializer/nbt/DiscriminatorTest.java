package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.NBT;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.TYPED;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertReads;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * The {@code type} field. Vanilla's {@code ComponentSerialization} reads it but never writes it, so
 * the shared serializer leaves it out and the {@code emitComponentType} variant puts it back.
 */
class DiscriminatorTest {

    @Nested
    @DisplayName("the shared serializer writes no type, matching vanilla's encoder")
    class DefaultOmitsType {

        @Test
        void keybind() {
            final NbtMap written = (NbtMap) NBT.serialize(Component.keybind("key.jump"));
            assertEquals(NbtMap.builder().putString("keybind", "key.jump").build(), written);
            assertFalse(written.containsKey("type"));
        }

        @Test
        void translatable() {
            final NbtMap written = (NbtMap) NBT.serialize(Component.translatable("a.key"));
            assertEquals(NbtMap.builder().putString("translate", "a.key").build(), written);
            assertFalse(written.containsKey("type"));
        }

        @Test
        void styledText() {
            final NbtMap written = (NbtMap) NBT.serialize(Component.text("hi", NamedTextColor.RED));
            assertEquals(NbtMap.builder().putString("text", "hi").putString("color", "red").build(), written);
            assertFalse(written.containsKey("type"));
        }
    }

    @Nested
    @DisplayName("emitComponentType(true) writes the type a vanilla client would otherwise infer")
    class BuilderEmitsType {

        @Test
        void keybind() {
            assertEquals(NbtMap.builder()
                .putString("type", "keybind")
                .putString("keybind", "key.jump")
                .build(), TYPED.serialize(Component.keybind("key.jump")));
        }

        @Test
        void translatable() {
            assertEquals(NbtMap.builder()
                .putString("type", "translatable")
                .putString("translate", "a.key")
                .build(), TYPED.serialize(Component.translatable("a.key")));
        }

        @Test
        void styledText() {
            assertEquals(NbtMap.builder()
                .putString("type", "text")
                .putString("text", "hi")
                .putString("color", "red")
                .build(), TYPED.serialize(Component.text("hi", NamedTextColor.RED)));
        }

        @Test
        void score() {
            assertEquals(NbtMap.builder()
                .putString("type", "score")
                .putCompound("score", NbtMap.builder()
                    .putString("name", "Steve")
                    .putString("objective", "deaths")
                    .build())
                .build(), TYPED.serialize(Component.score("Steve", "deaths")));
        }

        @Test
        void selector() {
            assertEquals(NbtMap.builder()
                .putString("type", "selector")
                .putString("selector", "@a")
                .build(), TYPED.serialize(Component.selector("@a")));
        }

        @Test
        @DisplayName("siblings are typed too")
        void siblingsAreTyped() {
            assertEquals(NbtMap.builder()
                .putString("type", "text")
                .putString("text", "a")
                .putString("color", "red")
                .putList("extra", NbtType.COMPOUND, List.of(NbtMap.builder()
                    .putString("type", "keybind")
                    .putString("keybind", "key.jump")
                    .build()))
                .build(), TYPED.serialize(Component.text("a", NamedTextColor.RED).append(Component.keybind("key.jump"))));
        }

        @Test
        @DisplayName("a plain literal still collapses to a bare string, which has nowhere to put a type")
        void bareStringsStayBare() {
            assertEquals("hi", TYPED.serialize(Component.text("hi")));
        }

        @Test
        @DisplayName("both forms read back the same, so either is understood by a vanilla client")
        void typedOutputReadsBackIdentically() {
            final Component component = Component.text("a", NamedTextColor.RED).append(Component.keybind("key.jump"));
            assertEquals(component, NBT.deserialize(TYPED.serialize(component)));
            assertEquals(component, NBT.deserialize(NBT.serialize(component)));
        }
    }

    @Nested
    @DisplayName("an explicit type wins over the field shape, as in vanilla's StrictEither")
    class ExplicitTypeWins {

        @Test
        void translatableTypeBeatsATextField() {
            assertReads(NbtMap.builder()
                .putString("type", "translatable")
                .putString("translate", "a")
                .putString("text", "b")
                .build(), Component.translatable("a"));
        }

        @Test
        void textTypeBeatsATranslateField() {
            assertReads(NbtMap.builder()
                .putString("type", "text")
                .putString("translate", "a")
                .putString("text", "b")
                .build(), Component.text("b"));
        }

        @Test
        void keybindTypeBeatsAnEarlierField() {
            assertReads(NbtMap.builder()
                .putString("type", "keybind")
                .putString("text", "b")
                .putString("keybind", "key.jump")
                .build(), Component.keybind("key.jump"));
        }
    }

    @Nested
    class UnknownType {

        @Test
        @DisplayName("an unknown discriminator falls back to field shape detection")
        void unknownTypeFallsBackToFields() {
            assertReads(NbtMap.builder()
                .putString("type", "definitely_not_a_vanilla_content_type")
                .putString("text", "hi")
                .build(), Component.text("hi"));
        }

        @Test
        void unknownTypeStillHonoursTheRegistrationOrder() {
            assertReads(NbtMap.builder()
                .putString("type", "something_else")
                .putString("translate", "a.key")
                .putString("keybind", "key.jump")
                .build(), Component.translatable("a.key"));
        }
    }

    @Nested
    @DisplayName("without a type, the first identifying field in vanilla's registration order wins")
    class FieldShapeDetection {

        @Test
        void textBeatsEverything() {
            assertReads(NbtMap.builder()
                .putString("text", "a")
                .putString("translate", "b")
                .putString("keybind", "key.jump")
                .build(), Component.text("a"));
        }

        @Test
        void translatableBeatsKeybind() {
            assertReads(NbtMap.builder()
                .putString("translate", "b")
                .putString("keybind", "key.jump")
                .build(), Component.translatable("b"));
        }

        @Test
        void keybindBeatsScore() {
            assertReads(NbtMap.builder()
                .putString("keybind", "key.jump")
                .putCompound("score", NbtMap.builder()
                    .putString("name", "Steve")
                    .putString("objective", "deaths")
                    .build())
                .build(), Component.keybind("key.jump"));
        }

        @Test
        void scoreBeatsSelector() {
            assertReads(NbtMap.builder()
                .putCompound("score", NbtMap.builder()
                    .putString("name", "Steve")
                    .putString("objective", "deaths")
                    .build())
                .putString("selector", "@a")
                .build(), Component.score("Steve", "deaths"));
        }

        @Test
        void selectorBeatsNbt() {
            assertReads(NbtMap.builder()
                .putString("selector", "@a")
                .putString("nbt", "Items")
                .putString("entity", "@s")
                .build(), Component.selector("@a"));
        }
    }
}
