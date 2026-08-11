package org.geysermc.mcprotocollib.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.ChatFormatting;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;

/**
 * Translatable components, including the mixed-type {@code with} array that vanilla stores as a list of
 * {@code {"": value}} wrappers.
 */
public class TranslatableTests {

    @GameTest
    public void noArguments(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.translatable("chat.type.text"));
    }

    @GameTest
    public void fallback(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper,
            Component.translatableWithFallback("does.not.exist", "a fallback"));
    }

    @GameTest
    public void componentArguments(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.translatable("chat.type.text",
            Component.literal("Steve"), Component.literal("hello")));
    }

    @GameTest
    public void stringArguments(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper,
            Component.translatable("chat.type.text", "Steve", "hello"));
    }

    /**
     * The interesting one: vanilla allows any mix of numbers, booleans, strings and components, and nbt
     * lists are homogeneous, so this goes over the wire as wrapped compounds.
     */
    @GameTest
    public void mixedArguments(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.translatable("test.mixed",
            5, 1.5D, 2.5F, 7L, (byte) 3, (short) 9, true, false, "a string",
            Component.literal("a component").withStyle(ChatFormatting.GOLD)));
    }

    @GameTest
    public void numericArgumentsOnly(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.translatable("test.numbers", 1, 2, 3));
    }

    @GameTest
    public void booleanArgumentsOnly(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.translatable("test.booleans", true, false));
    }

    @GameTest
    public void nestedTranslatable(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.translatable("outer",
            Component.translatable("inner", Component.literal("deep"))));
    }
}
