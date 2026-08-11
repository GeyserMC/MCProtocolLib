package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.NamedTextColor;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.NBT;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertReads;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertRoundTrips;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertWrites;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TranslatableTest {

    private static TranslatableComponent translatable(final String key, final TranslationArgument... arguments) {
        return Component.translatable().key(key).arguments(List.of(arguments)).build();
    }

    @Nested
    class KeyAndFallback {

        @Test
        @DisplayName("a translatable with no arguments writes no with key at all")
        void noArguments() {
            final Component component = Component.translatable("block.minecraft.stone");
            final NbtMap expected = NbtMap.builder().putString("translate", "block.minecraft.stone").build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);

            assertFalse(((NbtMap) NBT.serialize(component)).containsKey("with"));
        }

        @Test
        void fallback() {
            final Component component = Component.translatable("some.missing.key", "Some Missing Key");
            final NbtMap expected = NbtMap.builder()
                .putString("translate", "some.missing.key")
                .putString("fallback", "Some Missing Key")
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        @DisplayName("an absent fallback stays absent rather than becoming an empty string")
        void noFallback() {
            assertFalse(((NbtMap) NBT.serialize(Component.translatable("a.key"))).containsKey("fallback"));
            assertNull(((TranslatableComponent) NBT.deserialize(
                NbtMap.builder().putString("translate", "a.key").build())).fallback());
        }

        @Test
        void translatableCarriesStyleAndSiblings() {
            final Component component = Component.translatable()
                .key("chat.type.text")
                .color(NamedTextColor.GREEN)
                .append(Component.text("!"))
                .build();

            assertWrites(component, NbtMap.builder()
                .putString("translate", "chat.type.text")
                .putString("color", "green")
                .putList("extra", NbtType.STRING, List.of("!"))
                .build());
            assertRoundTrips(component);
        }
    }

    @Nested
    class Arguments {

        @Test
        void componentArguments() {
            final Component component = translatable("chat.type.text",
                TranslationArgument.component(Component.text("Steve", NamedTextColor.YELLOW)),
                TranslationArgument.component(Component.text("hi")));

            final NbtMap expected = NbtMap.builder()
                .putString("translate", "chat.type.text")
                .putList("with", NbtType.COMPOUND, List.of(
                    NbtMap.builder().putString("text", "Steve").putString("color", "yellow").build(),
                    // "hi" is a plain literal, so it wraps rather than collapsing next to a compound
                    NbtMap.builder().putString("", "hi").build()))
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        @DisplayName("a raw string entry in with is a plain literal argument")
        void stringArguments() {
            final Component component = translatable("chat.type.text",
                TranslationArgument.component(Component.text("Steve")),
                TranslationArgument.component(Component.text("hi")));

            final NbtMap expected = NbtMap.builder()
                .putString("translate", "chat.type.text")
                .putList("with", NbtType.STRING, List.of("Steve", "hi"))
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }
    }

    @Nested
    @DisplayName("numeric arguments keep the concrete tag type they were written with")
    class NumericArguments {

        @Test
        void byteArgument() {
            final Component component = translatable("a.key", TranslationArgument.numeric((byte) 7));
            final NbtMap expected = NbtMap.builder()
                .putString("translate", "a.key")
                .putList("with", NbtType.BYTE, List.of((byte) 7))
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        void intArgument() {
            final Component component = translatable("a.key", TranslationArgument.numeric(7));
            final NbtMap expected = NbtMap.builder()
                .putString("translate", "a.key")
                .putList("with", NbtType.INT, List.of(7))
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        void longArgument() {
            final Component component = translatable("a.key", TranslationArgument.numeric(7L));
            final NbtMap expected = NbtMap.builder()
                .putString("translate", "a.key")
                .putList("with", NbtType.LONG, List.of(7L))
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        void floatArgument() {
            final Component component = translatable("a.key", TranslationArgument.numeric(0.5f));
            final NbtMap expected = NbtMap.builder()
                .putString("translate", "a.key")
                .putList("with", NbtType.FLOAT, List.of(0.5f))
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        void doubleArgument() {
            final Component component = translatable("a.key", TranslationArgument.numeric(0.5d));
            final NbtMap expected = NbtMap.builder()
                .putString("translate", "a.key")
                .putList("with", NbtType.DOUBLE, List.of(0.5d))
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        @DisplayName("an int argument does not read back as a byte, or the other way round")
        void numericTypesAreDistinct() {
            assertNotEquals(translatable("a.key", TranslationArgument.numeric(7)),
                translatable("a.key", TranslationArgument.numeric((byte) 7)));

            assertReads(NbtMap.builder()
                .putString("translate", "a.key")
                .putList("with", NbtType.INT, List.of(7))
                .build(), translatable("a.key", TranslationArgument.numeric(7)));
        }
    }

    @Nested
    @DisplayName("a mixed with list uses vanilla's wrapped compound form, per ListTag#addAndUnwrap")
    class MixedArgumentLists {

        private static final NbtMap MIXED = NbtMap.builder()
            .putString("translate", "a.key")
            .putList("with", NbtType.COMPOUND, List.of(
                NbtMap.builder().putString("", "Steve").build(),
                NbtMap.builder().putInt("", 7).build()))
            .build();

        @Test
        void readingUnwrapsTheEntries() {
            assertReads(MIXED, translatable("a.key",
                TranslationArgument.component(Component.text("Steve")),
                TranslationArgument.numeric(7)));
        }

        @Test
        void writingWrapsTheEntries() {
            assertWrites(translatable("a.key",
                TranslationArgument.component(Component.text("Steve")),
                TranslationArgument.numeric(7)), MIXED);
        }

        @Test
        void mixedListsRoundTrip() {
            assertRoundTrips(translatable("a.key",
                TranslationArgument.component(Component.text("Steve")),
                TranslationArgument.numeric(7),
                TranslationArgument.numeric(0.5d),
                TranslationArgument.component(Component.text("!", NamedTextColor.RED))));
        }
    }

    @Nested
    class Nesting {

        @Test
        void translatableInsideTranslatable() {
            final Component component = translatable("outer.key",
                TranslationArgument.component(translatable("inner.key",
                    TranslationArgument.component(Component.text("deep")))));

            final NbtMap expected = NbtMap.builder()
                .putString("translate", "outer.key")
                .putList("with", NbtType.COMPOUND, List.of(NbtMap.builder()
                    .putString("translate", "inner.key")
                    .putList("with", NbtType.STRING, List.of("deep"))
                    .build()))
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }
    }

    @Nested
    class KnownAsymmetries {

        @Test
        @DisplayName("a boolean argument comes back as a byte, because nbt has no boolean type")
        void booleanArgumentsBecomeBytes() {
            final Component written = translatable("a.key", TranslationArgument.bool(true));

            // Vanilla's NbtOps#createBoolean writes a byte, and nothing in the tag records that the
            // value was meant as a boolean, so the read side cannot tell it apart from numeric(1b)
            assertWrites(written, NbtMap.builder()
                .putString("translate", "a.key")
                .putList("with", NbtType.BYTE, List.of((byte) 1))
                .build());

            final Component read = NBT.deserialize(NBT.serialize(written));
            assertEquals(translatable("a.key", TranslationArgument.numeric((byte) 1)), read);
            assertNotEquals(written, read);
        }

        @Test
        void falseBooleanArgumentBecomesZeroByte() {
            assertWrites(translatable("a.key", TranslationArgument.bool(false)), NbtMap.builder()
                .putString("translate", "a.key")
                .putList("with", NbtType.BYTE, List.of((byte) 0))
                .build());
            assertReads(NbtMap.builder()
                .putString("translate", "a.key")
                .putList("with", NbtType.BYTE, List.of((byte) 0))
                .build(), translatable("a.key", TranslationArgument.numeric((byte) 0)));
        }
    }
}
