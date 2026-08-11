package org.geysermc.mcprotocollib.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;

/**
 * The content types other than text and translatable.
 */
public class ContentTypeTests {

    @GameTest
    public void keybind(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.keybind("key.jump"));
    }

    @GameTest
    public void score(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, Component.score("Steve", "deaths"));
    }

    @GameTest
    public void scoreWithSelector(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.parse(helper, """
            {"score":{"name":"@p","objective":"kills"}}"""));
    }

    @GameTest
    public void selector(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.parse(helper, """
            {"selector":"@e[type=minecraft:pig]"}"""));
    }

    @GameTest
    public void selectorWithSeparator(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.parse(helper, """
            {"selector":"@a","separator":{"text":", ","color":"gray"}}"""));
    }

    // "interpret" and "plain" are the two booleans on nbt components

    @GameTest
    public void nbtEntity(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.parse(helper, """
            {"nbt":"Inventory","entity":"@s"}"""));
    }

    @GameTest
    public void nbtEntityInterpreting(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.parse(helper, """
            {"nbt":"CustomName","entity":"@s","interpret":true}"""));
    }

    @GameTest
    public void nbtBlockPlain(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.parse(helper, """
            {"nbt":"Items","block":"1 2 3","plain":true}"""));
    }

    @GameTest
    public void nbtStorageWithSeparator(final GameTestHelper helper) {
        ComponentChecks.roundTrip(helper, ComponentChecks.parse(helper, """
            {"nbt":"foo.bar","storage":"minecraft:test","separator":{"text":" | "}}"""));
    }
}
