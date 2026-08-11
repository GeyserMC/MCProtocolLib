package org.geysermc.mcprotocollib.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.ChatFormatting;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

/**
 * Click and hover events, which are where the style carries nested components and opaque data.
 */
public class EventTests {

    private static final UUID TEST_UUID = UUID.fromString("9cba7f27-9f2d-4c0e-9d19-52b2f4f6f1f9");

    @GameTest
    public void openUrl(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper,
            ComponentChecks.styled(new ClickEvent.OpenUrl(URI.create("https://geysermc.org"))));
    }

    @GameTest
    public void runCommand(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper,
            ComponentChecks.styled(new ClickEvent.RunCommand("/say hello")));
    }

    @GameTest
    public void suggestCommand(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper,
            ComponentChecks.styled(new ClickEvent.SuggestCommand("/msg ")));
    }

    @GameTest
    public void copyToClipboard(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper,
            ComponentChecks.styled(new ClickEvent.CopyToClipboard("copied")));
    }

    @GameTest
    public void changePage(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.styled(new ClickEvent.ChangePage(4)));
    }

    @GameTest
    public void showText(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper,
            ComponentChecks.styled(new HoverEvent.ShowText(Component.literal("plain tooltip"))));
    }

    @GameTest
    public void showTextNested(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.styled(new HoverEvent.ShowText(
            Component.translatable("tooltip.key", Component.literal("arg"), 42, true)
                .withStyle(ChatFormatting.LIGHT_PURPLE))));
    }

    @GameTest
    public void showItem(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.styled(new HoverEvent.ShowItem(
            new ItemStackTemplate(Items.DIAMOND_SWORD))));
    }

    @GameTest
    public void showItemStacked(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.styled(new HoverEvent.ShowItem(
            new ItemStackTemplate(Items.STONE, 32))));
    }

    @GameTest
    public void showEntity(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.styled(new HoverEvent.ShowEntity(
            new HoverEvent.EntityTooltipInfo(EntityTypes.PIG, TEST_UUID, Optional.empty()))));
    }

    @GameTest
    public void showEntityNamed(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.styled(new HoverEvent.ShowEntity(
            new HoverEvent.EntityTooltipInfo(EntityTypes.CREEPER, TEST_UUID,
                Optional.of(Component.literal("Bob").withStyle(ChatFormatting.GREEN))))));
    }
}
