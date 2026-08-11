package org.geysermc.mcprotocollib.gametest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryOps;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentSerializer;

/**
 * Checks the {@code nbt-component-serializer} module against the game it is a reimplementation of.
 *
 * <p>Vanilla is the oracle throughout: nothing here asserts what the module <em>should</em> produce,
 * only that a real Minecraft server, running the real {@code ComponentSerialization} codec, cannot tell
 * the module's output apart from its own.
 *
 * <p>There is no json anywhere in the path being validated - the module goes straight from a tag to an
 * adventure {@link net.kyori.adventure.text.Component} and back. Json is only used to pretty print a
 * mismatch, because a diff of two {@code Tag#toString} outputs is close to unreadable.
 */
public final class ComponentRoundTrip {

    private static final NbtComponentSerializer NBT = NbtComponentSerializer.nbt();

    private static final Gson PRETTY = new GsonBuilder().setPrettyPrinting().create();

    private ComponentRoundTrip() {
    }

    /**
     * Encodes a vanilla component the way the server sends it, then holds the module to what vanilla
     * itself makes of those bytes.
     */
    public static void assertMatchesVanilla(final RegistryAccess registries, final Component original) {
        final byte[] wire = toWire(registries, original);

        // The baseline has to be vanilla's own read of the same wire bytes, not the original component.
        // Nbt has no boolean type, so a boolean translation argument goes out as 1b and comes back as
        // the number 1 - for vanilla too. Comparing against the original would hold the module to
        // information the wire format does not carry.
        final Component vanillaRead = ComponentSerialization.TRUSTED_STREAM_CODEC
            .decode(new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(wire), registries));

        assertMatchesVanilla(registries, vanillaRead, readWire(wire));
    }

    /**
     * Checks a hand built tag, for the shapes vanilla can read but never writes.
     *
     * <p>Vanilla's {@code StrictEither} reads a {@code type} discriminator but its encoder never emits
     * one, so no round trip starting from a vanilla component can reach that path. Senders that do
     * write it - ViaVersion, Geyser, and MCProtocolLib itself - can, and when {@code type} disagrees
     * with the fields present, the discriminator has to win, exactly as it does in vanilla.
     */
    public static void assertRawTagMatchesVanilla(final RegistryAccess registries, final Tag tag) {
        final RegistryOps<Tag> nbtOps = registries.createSerializationContext(NbtOps.INSTANCE);

        final Component vanillaRead = ComponentSerialization.CODEC.parse(nbtOps, tag)
            .getOrThrow(error -> new AssertionError("Bad test tag " + tag + ": " + error));

        assertMatchesVanilla(registries, vanillaRead, toCloudburstTag(tag));
    }

    /**
     * The whole assertion, in three steps:
     *
     * <ol>
     *     <li>the module reads the wire tag into an adventure component - this is the code under test;</li>
     *     <li>the module writes that component back out, and the tag it produces has to mean the same
     *     thing to vanilla as the tag vanilla would have written for its own read of the same bytes;</li>
     *     <li>those bytes, handed back to vanilla's stream codec, have to decode to the very component
     *     vanilla started from.</li>
     * </ol>
     *
     * <p>The two components involved are different types - vanilla's and adventure's - so the comparison
     * happens one level down, on the tags each side canonicalises to. Both sides go through vanilla's
     * {@code ComponentSerialization.CODEC} before being compared, so an encoding vanilla considers
     * equivalent counts as a match: nbt has more than one spelling for the same component, and the
     * module deliberately picks the explicit one in places - most visibly the {@code object}
     * discriminator, which vanilla reads but never writes. What the codec cannot paper over is the
     * component actually changing, which is what this is looking for.
     */
    private static void assertMatchesVanilla(
        final RegistryAccess registries,
        final Component vanillaRead,
        final Object wireTag
    ) {
        final RegistryOps<Tag> nbtOps = registries.createSerializationContext(NbtOps.INSTANCE);
        final Tag expected = ComponentSerialization.CODEC.encodeStart(nbtOps, vanillaRead)
            .getOrThrow(error -> new AssertionError("Vanilla could not encode its own component: " + error));

        final net.kyori.adventure.text.Component adventure = NBT.deserialize(wireTag);
        final Object produced = NBT.serialize(adventure);
        final Tag producedTag = toVanillaTag(produced);

        final Component reparsed = ComponentSerialization.CODEC.parse(nbtOps, producedTag)
            .getOrThrow(error -> new AssertionError("Vanilla could not read back the tag the module produced: "
                + error + "\n  produced tag: " + pretty(producedTag)));
        final Tag actual = ComponentSerialization.CODEC.encodeStart(nbtOps, reparsed).getOrThrow();

        if (!expected.equals(actual)) {
            throw new AssertionError("Component did not survive nbt -> adventure -> nbt"
                + "\n  expected (vanilla's own read): " + pretty(expected)
                + "\n  actual (canonicalised):        " + pretty(actual)
                + "\n  as produced by the module:     " + pretty(producedTag)
                + "\n  adventure component:           " + adventure);
        }

        // Same bytes, all the way back around: what the module writes has to be readable by vanilla as
        // the component it read in the first place.
        final ByteBuf buf = Unpooled.buffer();
        MinecraftTypes.writeAnyTag(buf, produced);

        final Component reread = ComponentSerialization.TRUSTED_STREAM_CODEC
            .decode(new RegistryFriendlyByteBuf(buf, registries));
        if (!vanillaRead.equals(reread)) {
            throw new AssertionError("Vanilla read the module's own bytes back as a different component"
                + "\n  expected: " + vanillaRead
                + "\n  actual:   " + reread);
        }
    }

    /**
     * Encodes the component exactly the way it goes over the network.
     */
    private static byte[] toWire(final RegistryAccess registries, final Component original) {
        final RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), registries);
        ComponentSerialization.TRUSTED_STREAM_CODEC.encode(buf, original);

        final byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return bytes;
    }

    private static Object readWire(final byte[] wire) {
        final Object tag = MinecraftTypes.readAnyTag(Unpooled.wrappedBuffer(wire));
        if (tag == null) {
            throw new AssertionError("Vanilla wrote an end tag where a component was expected");
        }
        return tag;
    }

    // The bridge between the two tag models. Both libraries already agree byte for byte on the network
    // form, so going through it is both the shortest and the least assumption laden route - and it
    // normalises the one representational difference along the way, since a heterogeneous list is
    // written as wrapped compounds by cloudburst and unwrapped again by vanilla's reader.

    /**
     * Turns a cloudburst tag - what the module produces - into the vanilla {@link Tag} the test compares.
     */
    private static Tag toVanillaTag(final Object tag) {
        final ByteBuf buf = Unpooled.buffer();
        MinecraftTypes.writeAnyTag(buf, tag);
        return ByteBufCodecs.TRUSTED_TAG.decode(buf);
    }

    /**
     * Turns a vanilla {@link Tag} into the cloudburst tag the module consumes.
     */
    private static Object toCloudburstTag(final Tag tag) {
        final ByteBuf buf = Unpooled.buffer();
        ByteBufCodecs.TRUSTED_TAG.encode(buf, tag);
        return readWire(readable(buf));
    }

    private static byte[] readable(final ByteBuf buf) {
        final byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return bytes;
    }

    /**
     * Only ever called to build a failure message: json is not part of the path being validated, it is
     * just the readable way to show two tags side by side.
     */
    private static String pretty(final Tag tag) {
        final JsonElement json = new Dynamic<>(NbtOps.INSTANCE, tag).convert(JsonOps.INSTANCE).getValue();
        return PRETTY.toJson(json);
    }
}
