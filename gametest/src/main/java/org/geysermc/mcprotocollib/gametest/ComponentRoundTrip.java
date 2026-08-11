package org.geysermc.mcprotocollib.gametest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryOps;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.codec.NbtComponentSerializer;
import org.geysermc.mcprotocollib.protocol.data.DefaultComponentSerializer;

/**
 *
 */
public final class ComponentRoundTrip {

    private static final Gson PRETTY = new GsonBuilder().setPrettyPrinting().create();

    private ComponentRoundTrip() {
    }

    /**
     * Tests whether MCPL reads components properly, and tests nbt -> json conversion in MCPL
     */
    public static void assertNbtToJson(final RegistryAccess registries, final Component original) {
        final RegistryOps<JsonElement> jsonOps = registries.createSerializationContext(JsonOps.INSTANCE);
        final byte[] wire = toWire(registries, original);

        // The baseline has to be vanilla's own read of the same wire bytes, not an encode of the
        // original component. Nbt has no boolean type, so a boolean translation argument goes out as
        // 1b and comes back as the number 1 - for vanilla too. Comparing against the original would
        // hold MCProtocolLib to information the wire format does not carry.
        final Component vanillaRead = ComponentSerialization.TRUSTED_STREAM_CODEC
            .decode(new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(wire), registries));
        final JsonElement expected = canonicalJson(jsonOps, vanillaRead);

        // Let MCPL parse the network version of the component, then turn into json representation
        final Object nbt = MinecraftTypes.readAnyTag(Unpooled.wrappedBuffer(wire));
        final JsonElement produced = NbtComponentSerializer.tagComponentToJson(nbt);

        final Component reparsed = ComponentSerialization.CODEC.parse(jsonOps, produced)
            .getOrThrow(error -> new AssertionError("Vanilla could not read back the json MCProtocolLib produced: "
                + error + "\n  produced json: " + PRETTY.toJson(produced)));

        assertSameJson("nbt -> json", expected, canonicalJson(jsonOps, reparsed), produced);
    }

    /**
     * Checks a hand built tag, for the shapes vanilla can read but never writes.
     *
     * <p>Vanilla's {@code StrictEither} reads a {@code type} discriminator but its encoder never emits
     * one, so no round trip starting from a vanilla component can reach that path. Senders that do
     * write it - ViaVersion, Geyser, and MCProtocolLib itself - can, and when {@code type} disagrees
     * with the fields present, vanilla goes by {@code type} while adventure goes by the fields.
     */
    public static void assertRawNbtMatchesVanilla(final RegistryAccess registries, final Tag tag) {
        final RegistryOps<JsonElement> jsonOps = registries.createSerializationContext(JsonOps.INSTANCE);
        final RegistryOps<Tag> nbtOps = registries.createSerializationContext(NbtOps.INSTANCE);

        final Component vanilla = ComponentSerialization.CODEC.parse(nbtOps, tag)
            .getOrThrow(error -> new AssertionError("Bad test tag " + tag + ": " + error));
        final JsonElement expected = canonicalJson(jsonOps, vanilla);

        final ByteBuf buf = Unpooled.buffer();
        ByteBufCodecs.TRUSTED_TAG.encode(buf, tag);

        final net.kyori.adventure.text.Component adventure = MinecraftTypes.readComponent(buf);
        final JsonElement adventureJson = DefaultComponentSerializer.get().serializeToTree(adventure);
        final Component reparsed = ComponentSerialization.CODEC.parse(jsonOps, adventureJson)
            .getOrThrow(error -> new AssertionError("Vanilla could not read back adventure's json: "
                + error + "\n  adventure json: " + PRETTY.toJson(adventureJson)));

        assertSameJson("discriminated nbt", expected, canonicalJson(jsonOps, reparsed), adventureJson);
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

    private static JsonElement canonicalJson(final RegistryOps<JsonElement> jsonOps, final Component component) {
        return ComponentSerialization.CODEC.encodeStart(jsonOps, component).getOrThrow();
    }

    private static void assertSameJson(
        final String stage,
        final JsonElement expected,
        final JsonElement actual,
        final JsonElement produced
    ) {
        if (expected.equals(actual)) {
            return;
        }

        throw new AssertionError("Component did not survive " + stage
            + "\n  expected (vanilla's own read): " + PRETTY.toJson(expected)
            + "\n  actual (canonicalised):        " + PRETTY.toJson(actual)
            + "\n  as produced by MCProtocolLib:  " + PRETTY.toJson(produced));
    }
}
