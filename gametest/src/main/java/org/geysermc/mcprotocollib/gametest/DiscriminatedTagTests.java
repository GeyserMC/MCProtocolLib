package org.geysermc.mcprotocollib.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

/**
 * Tags carrying an explicit {@code type} discriminator, which vanilla reads but never writes.
 *
 * <p>Vanilla's {@code StrictEither} reads a {@code type} discriminator but its encoder never emits one,
 * so no round trip starting from a vanilla component can reach that path. Senders that do write it -
 * ViaVersion, Geyser, and MCProtocolLib itself - can, and when {@code type} disagrees with the fields
 * present, vanilla goes by {@code type} while adventure goes by field order, so the conflicting fields
 * have to be dropped on the way to json.
 */
public class DiscriminatedTagTests {

    @GameTest
    public void typeAgreesWithFields(final GameTestHelper helper) {
        ComponentChecks.rawTag(helper, ComponentChecks.compound(tag -> {
            tag.putString("type", "text");
            tag.putString("text", "plain");
        }));
    }

    // Adventure checks "text" before "translate", so without dropping the stale field these read back
    // as the wrong content type

    @GameTest
    public void translatableShadowedByText(final GameTestHelper helper) {
        ComponentChecks.rawTag(helper, ComponentChecks.compound(tag -> {
            tag.putString("type", "translatable");
            tag.putString("translate", "the.real.key");
            tag.putString("text", "stale text field");
        }));
    }

    @GameTest
    public void keybindShadowedByText(final GameTestHelper helper) {
        ComponentChecks.rawTag(helper, ComponentChecks.compound(tag -> {
            tag.putString("type", "keybind");
            tag.putString("keybind", "key.jump");
            tag.putString("text", "stale text field");
        }));
    }

    @GameTest
    public void selectorShadowedByTranslate(final GameTestHelper helper) {
        ComponentChecks.rawTag(helper, ComponentChecks.compound(tag -> {
            tag.putString("type", "selector");
            tag.putString("selector", "@a");
            tag.putString("translate", "stale translate field");
        }));
    }

    @GameTest
    public void textShadowedByTranslate(final GameTestHelper helper) {
        ComponentChecks.rawTag(helper, ComponentChecks.compound(tag -> {
            tag.putString("type", "text");
            tag.putString("text", "the real text");
            tag.putString("translate", "stale translate field");
        }));
    }

    // The object content type nests a second discriminator under "object"

    @GameTest
    public void discriminatedSprite(final GameTestHelper helper) {
        ComponentChecks.rawTag(helper, ComponentChecks.compound(tag -> {
            tag.putString("type", "object");
            tag.putString("object", "atlas");
            tag.putString("sprite", "minecraft:stone");
        }));
    }

    @GameTest
    public void discriminatedPlayer(final GameTestHelper helper) {
        ComponentChecks.rawTag(helper, ComponentChecks.compound(tag -> {
            tag.putString("type", "object");
            tag.putString("object", "player");
            tag.putString("player", "Steve");
        }));
    }
}
