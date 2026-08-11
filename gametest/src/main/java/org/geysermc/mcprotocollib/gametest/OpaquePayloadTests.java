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
 * Custom click events, whose payload is arbitrary nbt chosen by whoever sent it.
 *
 * <p>Vanilla passes this data straight through without looking inside, so the serializer has to as
 * well - including when the payload happens to use the very field names a component is made of. The
 * payload survives as structured nbt in both directions: adventure holds it as a
 * {@code BinaryTagHolder}, which is a string plus the codec that produced it, and the module supplies
 * an snbt codec over cloudburst tags for exactly that.
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

        ComponentChecks.opaque(helper, ComponentChecks.styled(
            new ClickEvent.Custom(Identifier.withDefaultNamespace("test"), Optional.of(payload))));
    }

    /**
     * The same trap one level down, where a naive converter would recurse into the nested compound.
     */
    @GameTest
    public void nestedPayloadShapedLikeComponent(final GameTestHelper helper) {
        final CompoundTag payload = ComponentChecks.compound(tag -> {
            tag.putString("type", "text");
            tag.putString("text", "outer");
            tag.put("extra", ComponentChecks.compound(nested -> {
                nested.putString("type", "translatable");
                nested.putString("translate", "inner");
                nested.putByte("bold", (byte) 0);
                nested.putInt("color", 42);
            }));
        });

        ComponentChecks.opaque(helper, ComponentChecks.styled(
            new ClickEvent.Custom(Identifier.withDefaultNamespace("test"), Optional.of(payload))));
    }

    @GameTest
    public void payloadWithUnrelatedTypeField(final GameTestHelper helper) {
        final CompoundTag payload = ComponentChecks.compound(tag -> {
            tag.putString("type", "minecraft:not_a_component_type");
            tag.putString("text", "must survive");
        });

        ComponentChecks.opaque(helper, ComponentChecks.styled(
            new ClickEvent.Custom(Identifier.withDefaultNamespace("test"), Optional.of(payload))));
    }

    @GameTest
    public void payloadWithMixedList(final GameTestHelper helper) {
        final ListTag mixedList = new ListTag();
        mixedList.add(StringTag.valueOf("a string"));
        mixedList.add(IntTag.valueOf(7));

        final CompoundTag payload = ComponentChecks.compound(tag -> tag.put("values", mixedList));

        ComponentChecks.opaque(helper, ComponentChecks.styled(
            new ClickEvent.Custom(Identifier.withDefaultNamespace("test"), Optional.of(payload))));
    }

    /**
     * Every scalar type nbt has, so a payload cannot quietly lose a number's width on the way through
     * the snbt form adventure stores it in.
     */
    @GameTest
    public void payloadWithEveryScalarType(final GameTestHelper helper) {
        final CompoundTag payload = ComponentChecks.compound(tag -> {
            tag.putByte("a byte", (byte) -3);
            tag.putShort("a short", (short) 300);
            tag.putInt("an int", 70000);
            tag.putLong("a long", 5000000000L);
            tag.putFloat("a float", 1.5F);
            tag.putDouble("a double", 2.5D);
            tag.putString("a string", "quoted \" and \\ escaped");
            tag.putByteArray("bytes", new byte[]{1, 2, 3});
            tag.putIntArray("ints", new int[]{4, 5, 6});
            tag.putLongArray("longs", new long[]{7L, 8L, 9L});
        });

        ComponentChecks.opaque(helper, ComponentChecks.styled(
            new ClickEvent.Custom(Identifier.withDefaultNamespace("test"), Optional.of(payload))));
    }

    @GameTest
    public void payloadAbsent(final GameTestHelper helper) {
        ComponentChecks.opaque(helper, ComponentChecks.styled(
            new ClickEvent.Custom(Identifier.withDefaultNamespace("test"), Optional.empty())));
    }
}
