package org.geysermc.mcprotocollib.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.ChatFormatting;
import net.minecraft.data.AtlasIds;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.objects.AtlasSprite;
import net.minecraft.resources.Identifier;

/**
 * Object components, which are new enough that they exercise the {@code sprite} / {@code player} content
 * detection and the {@code hat} boolean.
 */
public class ObjectComponentTests {

    @GameTest
    public void spriteDefaultAtlas(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.object(
            new AtlasSprite(AtlasIds.BLOCKS, Identifier.withDefaultNamespace("stone"))));
    }

    @GameTest
    public void spriteWithFallback(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.object(
            new AtlasSprite(AtlasIds.BLOCKS, Identifier.withDefaultNamespace("diamond_block")),
            Component.literal("[diamond block]")));
    }

    @GameTest
    public void spriteStyled(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.object(
                new AtlasSprite(AtlasIds.BLOCKS, Identifier.withDefaultNamespace("dirt")))
            .withStyle(ChatFormatting.AQUA));
    }

    // "hat" defaults to true, so pin it both ways

    @GameTest
    public void playerHead(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.parse(helper, """
            {"player":"Steve"}"""));
    }

    @GameTest
    public void playerHeadNoHat(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.parse(helper, """
            {"player":"Steve","hat":false}"""));
    }

    @GameTest
    public void playerHeadExplicitHat(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.parse(helper, """
            {"player":"Steve","hat":true}"""));
    }

    @GameTest
    public void playerHeadWithFallback(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.parse(helper, """
            {"player":"Steve","fallback":{"text":"[Steve]"}}"""));
    }
}
