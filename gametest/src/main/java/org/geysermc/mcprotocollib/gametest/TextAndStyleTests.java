package org.geysermc.mcprotocollib.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.ChatFormatting;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;

/**
 * Plain text, nesting and every {@code Style} field.
 */
public class TextAndStyleTests {

    @GameTest
    public void empty(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.empty());
    }

    @GameTest
    public void literal(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.literal("Hello, world!"));
    }

    @GameTest
    public void literalWithUnicode(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.literal("äöü 你好 💀"));
    }

    @GameTest
    public void siblings(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.literal("parent")
            .append(Component.literal("first"))
            .append(Component.literal("second").append(Component.literal("nested"))));
    }

    @GameTest
    public void namedColour(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.literal("red").withStyle(ChatFormatting.RED));
    }

    @GameTest
    public void rgbColour(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.literal("rgb")
            .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x55AAFF))));
    }

    @GameTest
    public void shadowColour(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.literal("shadow")
            .setStyle(Style.EMPTY.withShadowColor(0x80FF0000)));
    }

    @GameTest
    public void noShadow(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.literal("no shadow")
            .setStyle(Style.EMPTY.withoutShadow()));
    }

    /**
     * Every boolean in Style, deliberately mixing true and false: nbt has no booleans, so these only
     * survive if the byte is mapped back to a json boolean.
     */
    @GameTest
    public void allDecorations(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.literal("decorated").setStyle(Style.EMPTY
            .withBold(true)
            .withItalic(false)
            .withUnderlined(true)
            .withStrikethrough(false)
            .withObfuscated(true)));
    }

    @GameTest
    public void insertion(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.literal("insert")
            .setStyle(Style.EMPTY.withInsertion("inserted text")));
    }

    @GameTest
    public void font(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.literal("font").setStyle(Style.EMPTY
            .withFont(new FontDescription.Resource(Identifier.withDefaultNamespace("alt")))));
    }

    /**
     * Every style field at once, so a regression that only shows up in combination is caught.
     */
    @GameTest
    public void kitchenSink(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.literal("kitchen sink").setStyle(Style.EMPTY
            .withColor(TextColor.fromRgb(0x123456))
            .withShadowColor(0x7F00FF00)
            .withBold(true)
            .withItalic(true)
            .withUnderlined(false)
            .withStrikethrough(true)
            .withObfuscated(false)
            .withInsertion("everything")
            .withFont(new FontDescription.Resource(Identifier.withDefaultNamespace("uniform")))
            .withClickEvent(new ClickEvent.SuggestCommand("/msg Steve "))
            .withHoverEvent(new HoverEvent.ShowText(Component.literal("tooltip")))));
    }
}
