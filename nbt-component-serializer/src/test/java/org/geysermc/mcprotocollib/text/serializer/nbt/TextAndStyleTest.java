package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextAndStyleTest {

    @Nested
    class Literals {

        @Test
        @DisplayName("an unstyled childless literal collapses to a bare string, as vanilla's ComponentSerialization#tryCollapseToString does")
        void collapsesToBareString() {
            assertWrites(Component.text("hello"), "hello");
            assertReads("hello", Component.text("hello"));
            assertRoundTrips(Component.text("hello"));
        }

        @Test
        void emptyContent() {
            assertWrites(Component.text(""), "");
            assertWrites(Component.empty(), "");
            assertReads("", Component.text(""));
            assertEquals(Component.empty(), NBT.deserialize(""));
        }

        @Test
        void unicodeContent() {
            final String content = "héllo ✦ 你好 😀";
            assertWrites(Component.text(content), content);
            assertReads(content, Component.text(content));
            assertRoundTrips(Component.text(content));
        }

        @Test
        @DisplayName("a literal that carries style is a compound, not a bare string")
        void styledLiteralIsACompound() {
            assertWrites(Component.text("hi", NamedTextColor.RED), NbtMap.builder()
                .putString("text", "hi")
                .putString("color", "red")
                .build());
        }

        @Test
        @DisplayName("a literal that carries siblings is a compound, not a bare string")
        void literalWithSiblingsIsACompound() {
            assertWrites(Component.text("a").append(Component.text("b")), NbtMap.builder()
                .putString("text", "a")
                .putList("extra", NbtType.STRING, List.of("b"))
                .build());
        }

        @Test
        void readsCompoundFormOfAPlainLiteral() {
            assertReads(NbtMap.builder().putString("text", "hi").build(), Component.text("hi"));
        }
    }

    @Nested
    class Siblings {

        @Test
        void flatSiblings() {
            final Component component = Component.text("a")
                .append(Component.text("b"))
                .append(Component.text("c"));

            assertWrites(component, NbtMap.builder()
                .putString("text", "a")
                .putList("extra", NbtType.STRING, List.of("b", "c"))
                .build());
            assertRoundTrips(component);
        }

        @Test
        void nestedSiblings() {
            final Component component = Component.text("a")
                .append(Component.text("b").append(Component.text("c")));

            assertWrites(component, NbtMap.builder()
                .putString("text", "a")
                .putList("extra", NbtType.COMPOUND, List.of(NbtMap.builder()
                    .putString("text", "b")
                    .putList("extra", NbtType.STRING, List.of("c"))
                    .build()))
                .build());
            assertRoundTrips(component);
        }

        @Test
        void siblingsKeepTheirOwnStyle() {
            final Component component = Component.text("a")
                .append(Component.text("b", NamedTextColor.BLUE));

            assertWrites(component, NbtMap.builder()
                .putString("text", "a")
                .putList("extra", NbtType.COMPOUND, List.of(NbtMap.builder()
                    .putString("text", "b")
                    .putString("color", "blue")
                    .build()))
                .build());
            assertRoundTrips(component);
        }

        @Test
        void readsSiblingsFromExtra() {
            assertReads(NbtMap.builder()
                .putString("text", "a")
                .putList("extra", NbtType.STRING, List.of("b", "c"))
                .build(), Component.text("a")
                .append(Component.text("b"))
                .append(Component.text("c")));
        }
    }

    @Nested
    @DisplayName("the list shorthand, mirroring vanilla's ComponentSerialization#createFromList")
    class ListShorthand {

        @Test
        void firstEntryIsTheComponentAndTheRestAreItsSiblings() {
            assertReads(new NbtList<>(NbtType.STRING, List.of("a", "b", "c")), Component.text("a")
                .children(List.of(Component.text("b"), Component.text("c"))));
        }

        @Test
        void singleEntryListIsJustThatComponent() {
            assertReads(new NbtList<>(NbtType.STRING, List.of("a")), Component.text("a"));
        }

        @Test
        void listEntriesKeepTheirOwnSiblings() {
            final NbtMap first = NbtMap.builder()
                .putString("text", "a")
                .putList("extra", NbtType.STRING, List.of("b"))
                .build();

            assertReads(new NbtList<>(NbtType.COMPOUND, List.of(first, NbtMap.builder().putString("text", "c").build())),
                Component.text("a").children(List.of(Component.text("b"), Component.text("c"))));
        }

        @Test
        void emptyListIsNotAComponent() {
            final NbtSerializationException thrown = assertThrows(NbtSerializationException.class,
                () -> NBT.deserialize(new NbtList<>(NbtType.END, List.of())));
            assertEquals("A component written as a list must not be empty", thrown.getMessage());
        }

        @Test
        @DisplayName("the list shorthand is only a read form; writing always uses extra")
        void writingNeverProducesAList() {
            assertWrites(Component.text("a").append(Component.text("b")), NbtMap.builder()
                .putString("text", "a")
                .putList("extra", NbtType.STRING, List.of("b"))
                .build());
        }
    }

    @Nested
    class StyleFields {

        @Test
        void emptyStyleIsAnEmptyCompound() {
            assertEquals(NbtMap.EMPTY, NBT.serializeStyle(Style.empty()));
            assertEquals(Style.empty(), NBT.deserializeStyle(NbtMap.EMPTY));
        }

        @Test
        void namedColour() {
            assertEquals(NbtMap.builder().putString("color", "red").build(),
                NBT.serializeStyle(Style.style(NamedTextColor.RED)));
            assertEquals(Style.style(NamedTextColor.RED),
                NBT.deserializeStyle(NbtMap.builder().putString("color", "red").build()));
        }

        @Test
        @DisplayName("a colour with no name is written as #RRGGBB")
        void hexColour() {
            final TextColor colour = TextColor.color(0x1a2b3c);
            // Vanilla's TextColor#formatValue writes "#%06X", so the digits come out upper case
            assertEquals(NbtMap.builder().putString("color", "#1A2B3C").build(),
                NBT.serializeStyle(Style.style(colour)));
            // Parsing is case insensitive either way
            assertEquals(Style.style(colour),
                NBT.deserializeStyle(NbtMap.builder().putString("color", "#1a2b3c").build()));
        }

        @Test
        @DisplayName("shadow_color is a packed ARGB int, as vanilla's Style.Serializer writes it")
        void shadowColour() {
            final Style style = Style.style().shadowColor(ShadowColor.shadowColor(0x80112233)).build();
            assertEquals(NbtMap.builder().putInt("shadow_color", 0x80112233).build(), NBT.serializeStyle(style));
            assertEquals(style, NBT.deserializeStyle(NbtMap.builder().putInt("shadow_color", 0x80112233).build()));
        }

        @Test
        void font() {
            final Style style = Style.style().font(Key.key("minecraft:uniform")).build();
            assertEquals(NbtMap.builder().putString("font", "minecraft:uniform").build(), NBT.serializeStyle(style));
            assertEquals(style, NBT.deserializeStyle(NbtMap.builder().putString("font", "minecraft:uniform").build()));
        }

        @Test
        void insertion() {
            final Style style = Style.style().insertion("/say hi").build();
            assertEquals(NbtMap.builder().putString("insertion", "/say hi").build(), NBT.serializeStyle(style));
            assertEquals(style, NBT.deserializeStyle(NbtMap.builder().putString("insertion", "/say hi").build()));
        }
    }

    @Nested
    @DisplayName("decorations are tri-state: absent, false, or true")
    class Decorations {

        @Test
        void allFiveDecorationsWhenTrue() {
            final Style style = Style.style()
                .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.TRUE)
                .decoration(TextDecoration.UNDERLINED, TextDecoration.State.TRUE)
                .decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.TRUE)
                .decoration(TextDecoration.OBFUSCATED, TextDecoration.State.TRUE)
                .build();

            final NbtMap expected = NbtMap.builder()
                .putBoolean("bold", true)
                .putBoolean("italic", true)
                .putBoolean("underlined", true)
                .putBoolean("strikethrough", true)
                .putBoolean("obfuscated", true)
                .build();

            assertEquals(expected, NBT.serializeStyle(style));
            assertEquals(style, NBT.deserializeStyle(expected));
        }

        @Test
        void allFiveDecorationsWhenFalse() {
            final Style style = Style.style()
                .decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                .decoration(TextDecoration.UNDERLINED, TextDecoration.State.FALSE)
                .decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.FALSE)
                .decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE)
                .build();

            final NbtMap expected = NbtMap.builder()
                .putBoolean("bold", false)
                .putBoolean("italic", false)
                .putBoolean("underlined", false)
                .putBoolean("strikethrough", false)
                .putBoolean("obfuscated", false)
                .build();

            assertEquals(expected, NBT.serializeStyle(style));
            assertEquals(style, NBT.deserializeStyle(expected));
        }

        @Test
        @DisplayName("a NOT_SET decoration writes no key at all, so a child can still inherit it")
        void notSetWritesNoKey() {
            final Style style = Style.style()
                .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET)
                .build();

            final NbtMap written = NBT.serializeStyle(style);
            assertEquals(NbtMap.builder().putBoolean("bold", true).build(), written);
            assertFalse(written.containsKey("italic"));
            assertFalse(written.containsKey("underlined"));
            assertFalse(written.containsKey("strikethrough"));
            assertFalse(written.containsKey("obfuscated"));
        }

        @Test
        @DisplayName("absent, false and true are three distinct states, not two")
        void absentIsNotTheSameAsFalse() {
            final Style absent = Style.style().decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET).build();
            final Style off = Style.style().decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).build();
            final Style on = Style.style().decoration(TextDecoration.BOLD, TextDecoration.State.TRUE).build();

            assertEquals(NbtMap.EMPTY, NBT.serializeStyle(absent));
            assertEquals(NbtMap.builder().putBoolean("bold", false).build(), NBT.serializeStyle(off));
            assertEquals(NbtMap.builder().putBoolean("bold", true).build(), NBT.serializeStyle(on));

            assertEquals(TextDecoration.State.NOT_SET, NBT.deserializeStyle(NbtMap.EMPTY).decoration(TextDecoration.BOLD));
            assertEquals(TextDecoration.State.FALSE,
                NBT.deserializeStyle(NbtMap.builder().putBoolean("bold", false).build()).decoration(TextDecoration.BOLD));
            assertEquals(TextDecoration.State.TRUE,
                NBT.deserializeStyle(NbtMap.builder().putBoolean("bold", true).build()).decoration(TextDecoration.BOLD));
        }
    }

    @Nested
    class KitchenSink {

        @Test
        @DisplayName("content, every plain style field and siblings in one component")
        void everythingAtOnce() {
            final Component component = Component.text()
                .content("hello")
                .color(NamedTextColor.GOLD)
                .shadowColor(ShadowColor.shadowColor(0x80112233))
                .font(Key.key("minecraft:uniform"))
                .insertion("/say hi")
                .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                .decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET)
                .append(Component.text("world", NamedTextColor.AQUA))
                .append(Component.text("!"))
                .build();

            final NbtMap expected = NbtMap.builder()
                .putString("text", "hello")
                .putString("color", "gold")
                .putInt("shadow_color", 0x80112233)
                .putString("font", "minecraft:uniform")
                .putString("insertion", "/say hi")
                .putBoolean("bold", true)
                .putBoolean("italic", false)
                // The styled sibling is a compound but the plain one collapses to a string, so the
                // list is heterogeneous and vanilla's ListTag#write wraps the odd one out
                .putList("extra", NbtType.COMPOUND, List.of(
                    NbtMap.builder().putString("text", "world").putString("color", "aqua").build(),
                    NbtMap.builder().putString("", "!").build()))
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        @DisplayName("a mixed extra list is written in vanilla's wrapped compound form")
        void mixedSiblingsUseTheWrappedListForm() {
            // "world" collapses to a bare string while the styled "!" stays a compound, so the two
            // entries have different tag types - see vanilla's ListTag#addAndUnwrap
            final Component component = Component.text("hello")
                .append(Component.text("world"))
                .append(Component.text("!", NamedTextColor.RED));

            final NbtMap expected = NbtMap.builder()
                .putString("text", "hello")
                .putList("extra", NbtType.COMPOUND, List.of(
                    NbtMap.builder().putString("", "world").build(),
                    NbtMap.builder().putString("text", "!").putString("color", "red").build()))
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
        }
    }
}
