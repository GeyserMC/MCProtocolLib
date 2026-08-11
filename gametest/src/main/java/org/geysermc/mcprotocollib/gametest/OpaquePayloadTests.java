package org.geysermc.mcprotocollib.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * Components carrying opaque nbt that happens to use field names the converter cares about. Vanilla
 * passes this data straight through, so the converter has to as well.
 *
 * <p>Only checked at the nbt to json level: adventure models a custom click event's payload as an snbt
 * string rather than as structured data, which is a difference in adventure, not in the converter.
 */
public class OpaquePayloadTests {

    @GameTest
    public void payloadShapedLikeComponent(final GameTestHelper helper) {
        final CompoundTag payload = ComponentChecks.compound(tag -> {
            tag.putString("type", "translatable");
            tag.putString("text", "not a component");
            tag.putString("translate", "also not a component");
            tag.putByte("bold", (byte) 1);
        });

        ComponentChecks.nbtToJson(helper, ComponentChecks.styled(
            new ClickEvent.Custom(Identifier.withDefaultNamespace("test"), Optional.of(payload))));
    }

    @GameTest
    public void payloadWithUnrelatedTypeField(final GameTestHelper helper) {
        final CompoundTag payload = ComponentChecks.compound(tag -> {
            tag.putString("type", "minecraft:not_a_component_type");
            tag.putString("text", "must survive");
        });

        ComponentChecks.nbtToJson(helper, ComponentChecks.styled(
            new ClickEvent.Custom(Identifier.withDefaultNamespace("test"), Optional.of(payload))));
    }

    @GameTest
    public void payloadWithMixedList(final GameTestHelper helper) {
        final ListTag mixedList = new ListTag();
        mixedList.add(StringTag.valueOf("a string"));
        mixedList.add(IntTag.valueOf(7));

        final CompoundTag payload = ComponentChecks.compound(tag -> tag.put("values", mixedList));

        ComponentChecks.nbtToJson(helper, ComponentChecks.styled(
            new ClickEvent.Custom(Identifier.withDefaultNamespace("test"), Optional.of(payload))));
    }
}
