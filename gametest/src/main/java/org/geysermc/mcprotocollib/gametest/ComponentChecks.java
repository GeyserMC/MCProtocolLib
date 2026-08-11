package org.geysermc.mcprotocollib.gametest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import java.util.function.Consumer;

/**
 * What every case in this package does with the component it builds.
 *
 * <p>Each case is its own {@link net.fabricmc.fabric.api.gametest.v1.GameTest}, so a failure names the
 * exact component that broke rather than one entry out of a batch.
 */
public final class ComponentChecks {

    private ComponentChecks() {
    }

    /**
     * The full path MCProtocolLib uses at runtime: wire nbt to an adventure component and back to nbt,
     * checked against vanilla's own read of the same bytes.
     */
    public static void roundTrip(final GameTestHelper helper, final Component component) {
        ComponentRoundTrip.assertMatchesVanilla(helper.getLevel().registryAccess(), component);
        helper.succeed();
    }

    /**
     * The same check, for components whose payload is nbt the serializer must carry rather than
     * understand. Named apart from {@link #roundTrip} only so a failure says which kind of case broke.
     */
    public static void opaque(final GameTestHelper helper, final Component component) {
        ComponentRoundTrip.assertMatchesVanilla(helper.getLevel().registryAccess(), component);
        helper.succeed();
    }

    /**
     * A hand built tag, for the shapes vanilla can read but never writes.
     */
    public static void rawTag(final GameTestHelper helper, final Tag tag) {
        ComponentRoundTrip.assertRawTagMatchesVanilla(helper.getLevel().registryAccess(), tag);
        helper.succeed();
    }

    /**
     * A few content types ({@code selector}, {@code nbt}) hold a {@code CompilableString}, which can only
     * be produced by its codec, so those are built by parsing vanilla json with vanilla's codec - still
     * vanilla components, just assembled the only way the API allows.
     */
    public static Component parse(final GameTestHelper helper, final String json) {
        final JsonElement element = JsonParser.parseString(json);
        return ComponentSerialization.CODEC
            .parse(helper.getLevel().registryAccess().createSerializationContext(JsonOps.INSTANCE), element)
            .getOrThrow(error -> new IllegalStateException("Bad test component " + json + ": " + error));
    }

    public static Component styled(final ClickEvent clickEvent) {
        return Component.literal("click me").setStyle(Style.EMPTY.withClickEvent(clickEvent));
    }

    public static Component styled(final HoverEvent hoverEvent) {
        return Component.literal("hover me").setStyle(Style.EMPTY.withHoverEvent(hoverEvent));
    }

    public static CompoundTag compound(final Consumer<CompoundTag> builder) {
        final CompoundTag tag = new CompoundTag();
        builder.accept(tag);
        return tag;
    }
}
