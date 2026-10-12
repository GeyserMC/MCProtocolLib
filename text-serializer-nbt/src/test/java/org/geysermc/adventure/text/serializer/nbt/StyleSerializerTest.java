package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.List;

public class StyleSerializerTest {
    static final List<Arguments> STYLES = List.of(
        Arguments.arguments(
            NbtMap.builder()
                .putString("color", "dark_purple"),
            Style.style().color(NamedTextColor.DARK_PURPLE)
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("color", "#AACCEE"),
            Style.style().color(TextColor.fromHexString("#AACCEE"))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putInt("shadow_color", 0xAABBCCDD),
            Style.style().shadowColor(ShadowColor.shadowColor(0xAABBCCDD))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putBoolean("bold", true),
            Style.style().decorate(TextDecoration.BOLD)
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putBoolean("bold", true)
                .putBoolean("italic", true)
                .putBoolean("obfuscated", true),
            Style.style().decorate(TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.OBFUSCATED)
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putBoolean("bold", true)
                .putBoolean("italic", false)
                .putBoolean("underlined", false)
                .putBoolean("strikethrough", false)
                .putBoolean("obfuscated", true),
            Style.style()
                .decorate(TextDecoration.BOLD, TextDecoration.OBFUSCATED)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.UNDERLINED, false)
                .decoration(TextDecoration.STRIKETHROUGH, false)
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putCompound("click_event", NbtMap.builder()
                    .putString("action", "open_url")
                    .putString("url", "https://www.youtube.com/watch?v=_yqSbnbUsj4")
                    .build()
                ),
            Style.style().clickEvent(ClickEvent.openUrl("https://www.youtube.com/watch?v=_yqSbnbUsj4"))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putCompound("hover_event", NbtMap.builder()
                    .putString("action", "show_text")
                    .putString("value", "I love cats.")
                    .build()),
            Style.style().hoverEvent(HoverEvent.showText(Component.text("I love cats.")))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("insertion", "h"),
            Style.style().insertion("h")
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("font", "geyser:my_super_cool_font"),
            Style.style().font(Key.key("geyser", "my_super_cool_font"))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("color", "dark_aqua")
                .putInt("shadow_color", 0x12345678)
                .putBoolean("bold", false)
                .putBoolean("italic", true)
                .putBoolean("underlined", true)
                .putBoolean("strikethrough", true)
                .putBoolean("obfuscated", false)
                .putCompound("click_event", NbtMap.builder()
                    .putString("action", "open_url")
                    .putString("url", "https://www.youtube.com/watch?v=FtutLA63Cp8")
                    .build()
                )
                .putCompound("hover_event", NbtMap.builder()
                    .putString("action", "show_text")
                    .putString("value", "yummy")
                    .build())
                .putString("insertion", ",")
                .putString("font", "geyser:less_cool_font"),
            Style.style()
                .color(NamedTextColor.DARK_AQUA)
                .shadowColor(ShadowColor.shadowColor(0x12345678))
                .decoration(TextDecoration.BOLD, false)
                .decoration(TextDecoration.ITALIC, true)
                .decoration(TextDecoration.UNDERLINED, true)
                .decoration(TextDecoration.STRIKETHROUGH, true)
                .decoration(TextDecoration.OBFUSCATED, false)
                .clickEvent(ClickEvent.openUrl("https://www.youtube.com/watch?v=FtutLA63Cp8"))
                .hoverEvent(HoverEvent.showText(Component.text("yummy")))
                .insertion(",")
                .font(Key.key("geyser", "less_cool_font"))
        )
    );

    @ParameterizedTest
    @FieldSource("STYLES")
    void testDeserialize(NbtMapBuilder map, Style.Builder result) {
        Assertions.assertEquals(result.build(), StyleSerializerImpl.deserialize(map.build(), NbtComponentSerializer.nbt()));
    }

    @ParameterizedTest
    @FieldSource("STYLES")
    void testSerialize(NbtMapBuilder result, Style.Builder style) {
        NbtMapBuilder builder = NbtMap.builder();
        StyleSerializerImpl.serialize(builder, style.build(), NbtComponentSerializer.nbt());
        Assertions.assertEquals(result.build(), builder.build());
    }
}
