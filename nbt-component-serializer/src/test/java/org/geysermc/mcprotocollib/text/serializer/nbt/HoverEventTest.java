package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertReads;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertRoundTrips;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertWrites;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The {@code hover_event} compound. Since 1.21.5 the action's fields sit beside {@code action} rather
 * than inside a {@code contents} wrapper.
 */
class HoverEventTest {

    private static final HoverEventSerializer HOVER = new HoverEventSerializer(new NbtComponentSerializerImpl(false));

    private static NbtMap action(final String name) {
        return NbtMap.builder().putString("action", name).build();
    }

    private static String readMessage(final NbtMap tag) {
        return assertThrows(NbtSerializationException.class, () -> HOVER.deserialize(tag)).getMessage();
    }

    private static Component styled(final HoverEvent<?> event) {
        return Component.text("hover me").hoverEvent(event);
    }

    private static NbtMap styledTag(final NbtMap event) {
        return NbtMap.builder()
            .putString("text", "hover me")
            .putCompound("hover_event", event)
            .build();
    }

    @Nested
    class ShowText {

        @Test
        @DisplayName("a plain value collapses to a bare string, as any other component would")
        void plainString() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "show_text")
                .putString("value", "a tooltip")
                .build();
            final HoverEvent<?> event = HoverEvent.showText(Component.text("a tooltip"));

            assertEquals(expected, HOVER.serialize(event));
            assertEquals(event, HOVER.deserialize(expected));

            assertWrites(styled(event), styledTag(expected));
            assertReads(styledTag(expected), styled(event));
            assertRoundTrips(styled(event));
        }

        @Test
        @DisplayName("a value can be any component, nesting and all")
        void nestedComponentValue() {
            final Component value = Component.translatable("block.minecraft.stone", NamedTextColor.GOLD)
                .append(Component.text("!"));
            final NbtMap expected = NbtMap.builder()
                .putString("action", "show_text")
                .putCompound("value", NbtMap.builder()
                    .putString("translate", "block.minecraft.stone")
                    .putString("color", "gold")
                    .putList("extra", NbtType.STRING, List.of("!"))
                    .build())
                .build();
            final HoverEvent<?> event = HoverEvent.showText(value);

            assertEquals(expected, HOVER.serialize(event));
            assertEquals(event, HOVER.deserialize(expected));
            assertRoundTrips(styled(event));
        }

        @Test
        void missingValueIsRejected() {
            assertEquals("Missing required component field 'value'", readMessage(action("show_text")));
        }
    }

    @Nested
    class ShowItem {

        private static final Key STONE = Key.key("minecraft:stone");

        @Test
        @DisplayName("a single item writes no count, because vanilla's ItemStack count defaults to one")
        void idOnly() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "show_item")
                .putString("id", "minecraft:stone")
                .build();
            final HoverEvent<?> event = HoverEvent.showItem(STONE, 1);

            final NbtMap written = HOVER.serialize(event);
            assertEquals(expected, written);
            assertFalse(written.containsKey("count"));
            assertEquals(event, HOVER.deserialize(expected));
            assertRoundTrips(styled(event));
        }

        @Test
        @DisplayName("an absent count reads as one")
        void absentCountDefaultsToOne() {
            final HoverEvent<?> event = HOVER.deserialize(
                NbtMap.builder().putString("action", "show_item").putString("id", "minecraft:stone").build());
            assertEquals(1, ((HoverEvent.ShowItem) event.value()).count());
        }

        @Test
        void idAndCount() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "show_item")
                .putString("id", "minecraft:stone")
                .putInt("count", 42)
                .build();
            final HoverEvent<?> event = HoverEvent.showItem(STONE, 42);

            assertEquals(expected, HOVER.serialize(event));
            assertEquals(event, HOVER.deserialize(expected));
            assertWrites(styled(event), styledTag(expected));
            assertRoundTrips(styled(event));
        }

        @Test
        @DisplayName("vanilla's count range is 1..99")
        void countsOutsideTheRangeAreRejected() {
            assertEquals("A show_item hover event needs a count between 1 and 99, got 0",
                readMessage(NbtMap.builder()
                    .putString("action", "show_item")
                    .putString("id", "minecraft:stone")
                    .putInt("count", 0)
                    .build()));
            assertEquals("A show_item hover event needs a count between 1 and 99, got -3",
                readMessage(NbtMap.builder()
                    .putString("action", "show_item")
                    .putString("id", "minecraft:stone")
                    .putInt("count", -3)
                    .build()));
            assertEquals("A show_item hover event needs a count between 1 and 99, got 100",
                readMessage(NbtMap.builder()
                    .putString("action", "show_item")
                    .putString("id", "minecraft:stone")
                    .putInt("count", 100)
                    .build()));
        }

        @Test
        void countNinetyNineIsStillValid() {
            final HoverEvent<?> event = HoverEvent.showItem(STONE, 99);
            assertEquals(event, HOVER.deserialize(NbtMap.builder()
                .putString("action", "show_item")
                .putString("id", "minecraft:stone")
                .putInt("count", 99)
                .build()));
        }

        @Test
        void missingIdIsRejected() {
            assertEquals("Missing required string field 'id'", readMessage(action("show_item")));
        }
    }

    @Nested
    @DisplayName("the item's data components, which are opaque to a text component serializer")
    class DataComponents {

        private static final NbtMap CUSTOM_DATA = NbtMap.builder()
            .putString("text", "not a component")
            .putInt("uses", 3)
            .putCompound("nested", NbtMap.builder().putByte("flag", (byte) 1).build())
            .build();

        private static NbtMap tagWithComponents() {
            return NbtMap.builder()
                .putString("action", "show_item")
                .putString("id", "minecraft:stone")
                .putCompound("components", NbtMap.builder()
                    .putCompound("minecraft:custom_data", CUSTOM_DATA)
                    .putString("minecraft:item_name", "Fancy Rock")
                    .build())
                .build();
        }

        private static HoverEvent<HoverEvent.ShowItem> eventWithComponents() {
            final Map<Key, DataComponentValue> components = new LinkedHashMap<>();
            components.put(Key.key("minecraft:custom_data"), NbtDataComponentValue.nbtDataComponentValue(CUSTOM_DATA));
            components.put(Key.key("minecraft:item_name"), NbtDataComponentValue.nbtDataComponentValue("Fancy Rock"));
            return HoverEvent.showItem(Key.key("minecraft:stone"), 1, components);
        }

        @Test
        @DisplayName("values are kept as the tags they were read from, never reinterpreted")
        void opaqueValuesSurvive() {
            assertEquals(eventWithComponents(), HOVER.deserialize(tagWithComponents()));
            assertEquals(tagWithComponents(), HOVER.serialize(eventWithComponents()));
            assertEquals(tagWithComponents(), HOVER.serialize(HOVER.deserialize(tagWithComponents())));
        }

        @Test
        void throughAComponent() {
            assertWrites(styled(eventWithComponents()), styledTag(tagWithComponents()));
            assertReads(styledTag(tagWithComponents()), styled(eventWithComponents()));
            assertRoundTrips(styled(eventWithComponents()));
        }

        @Test
        @DisplayName("no components at all writes no components key")
        void emptyComponentsAreOmitted() {
            final NbtMap written = HOVER.serialize(HoverEvent.showItem(Key.key("minecraft:stone"), 1, Map.of()));
            assertEquals(NbtMap.builder()
                .putString("action", "show_item")
                .putString("id", "minecraft:stone")
                .build(), written);
            assertFalse(written.containsKey("components"));
        }

        @Test
        @DisplayName("a ! prefixed key is vanilla's way of writing a removed component")
        void removedComponent() {
            final NbtMap tag = NbtMap.builder()
                .putString("action", "show_item")
                .putString("id", "minecraft:stone")
                .putCompound("components", NbtMap.builder()
                    .putCompound("!minecraft:custom_name", NbtMap.EMPTY)
                    .build())
                .build();
            final HoverEvent<HoverEvent.ShowItem> event = HoverEvent.showItem(Key.key("minecraft:stone"), 1,
                Map.of(Key.key("minecraft:custom_name"), DataComponentValue.removed()));

            assertEquals(event, HOVER.deserialize(tag));
            assertEquals(DataComponentValue.removed(), ((HoverEvent.ShowItem) HOVER.deserialize(tag).value())
                .dataComponents().get(Key.key("minecraft:custom_name")));
            assertEquals(tag, HOVER.serialize(event));
        }

        @Test
        @DisplayName("a value written against another nbt model comes in through its snbt string")
        void tagSerializableValue() {
            final HoverEvent<HoverEvent.ShowItem> event = HoverEvent.showItem(Key.key("minecraft:stone"), 1,
                Map.of(Key.key("minecraft:custom_data"), BinaryTagHolder.binaryTagHolder("{uses:3}")));

            assertEquals(NbtMap.builder()
                .putString("action", "show_item")
                .putString("id", "minecraft:stone")
                .putCompound("components", NbtMap.builder()
                    .putCompound("minecraft:custom_data", NbtMap.builder().putInt("uses", 3).build())
                    .build())
                .build(), HOVER.serialize(event));
        }
    }

    @Nested
    class ShowEntity {

        /**
         * Most significant bits 0x0011223344556677, least significant bits 0x8899AABBCCDDEEFF.
         */
        private static final UUID UUID_VALUE = UUID.fromString("00112233-4455-6677-8899-aabbccddeeff");

        /**
         * Vanilla's {@code UUIDUtil#uuidToIntArray} writes the most significant half first, high int
         * before low int, then the least significant half the same way.
         */
        private static final int[] UUID_INTS = {0x00112233, 0x44556677, 0x8899AABB, 0xCCDDEEFF};

        @Test
        @DisplayName("the uuid is written as vanilla's four ints, most significant half first")
        void uuidLayout() {
            assertEquals(0x0011223344556677L, UUID_VALUE.getMostSignificantBits());
            assertEquals(0x8899AABBCCDDEEFFL, UUID_VALUE.getLeastSignificantBits());

            final NbtMap written = HOVER.serialize(HoverEvent.showEntity(Key.key("minecraft:pig"), UUID_VALUE));
            assertArrayEquals(UUID_INTS, (int[]) written.get("uuid"));
        }

        @Test
        void idAndUuid() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "show_entity")
                .putString("id", "minecraft:pig")
                .putIntArray("uuid", UUID_INTS)
                .build();
            final HoverEvent<?> event = HoverEvent.showEntity(Key.key("minecraft:pig"), UUID_VALUE);

            assertEquals(expected, HOVER.serialize(event));
            assertEquals(event, HOVER.deserialize(expected));
            assertWrites(styled(event), styledTag(expected));
            assertReads(styledTag(expected), styled(event));
            assertRoundTrips(styled(event));
        }

        @Test
        void withAName() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "show_entity")
                .putString("id", "minecraft:pig")
                .putIntArray("uuid", UUID_INTS)
                .putCompound("name", NbtMap.builder()
                    .putString("text", "Porkchop")
                    .putString("color", "red")
                    .build())
                .build();
            final HoverEvent<?> event = HoverEvent.showEntity(Key.key("minecraft:pig"), UUID_VALUE,
                Component.text("Porkchop", NamedTextColor.RED));

            assertEquals(expected, HOVER.serialize(event));
            assertEquals(event, HOVER.deserialize(expected));
            assertRoundTrips(styled(event));
        }

        @Test
        @DisplayName("no name writes no name key")
        void nameIsOptional() {
            final NbtMap written = HOVER.serialize(HoverEvent.showEntity(Key.key("minecraft:pig"), UUID_VALUE));
            assertFalse(written.containsKey("name"));
        }

        @Test
        @DisplayName("a dashed string uuid is read too, as vanilla's UUIDUtil.LENIENT_CODEC does")
        void dashedStringUuidIsRead() {
            assertEquals(HoverEvent.showEntity(Key.key("minecraft:pig"), UUID_VALUE), HOVER.deserialize(NbtMap.builder()
                .putString("action", "show_entity")
                .putString("id", "minecraft:pig")
                .putString("uuid", "00112233-4455-6677-8899-aabbccddeeff")
                .build()));
        }

        @Test
        @DisplayName("but a string uuid is never written back, only the four int form")
        void stringUuidIsNeverWritten() {
            final NbtMap written = HOVER.serialize(HOVER.deserialize(NbtMap.builder()
                .putString("action", "show_entity")
                .putString("id", "minecraft:pig")
                .putString("uuid", "00112233-4455-6677-8899-aabbccddeeff")
                .build()));

            assertEquals(NbtMap.builder()
                .putString("action", "show_entity")
                .putString("id", "minecraft:pig")
                .putIntArray("uuid", UUID_INTS)
                .build(), written);
        }

        @Test
        void wrongLengthUuidIsRejected() {
            assertEquals("A uuid written as ints needs exactly 4 of them, got 3",
                readMessage(NbtMap.builder()
                    .putString("action", "show_entity")
                    .putString("id", "minecraft:pig")
                    .putIntArray("uuid", new int[]{1, 2, 3})
                    .build()));
        }

        @Test
        void unparseableStringUuidIsRejected() {
            assertEquals("Not a valid uuid: 'not-a-uuid'",
                readMessage(NbtMap.builder()
                    .putString("action", "show_entity")
                    .putString("id", "minecraft:pig")
                    .putString("uuid", "not-a-uuid")
                    .build()));
        }

        @Test
        void missingUuidIsRejected() {
            assertEquals("Expected field 'uuid' to be an int array or a string, got nothing",
                readMessage(NbtMap.builder()
                    .putString("action", "show_entity")
                    .putString("id", "minecraft:pig")
                    .build()));
        }

        @Test
        void missingIdIsRejected() {
            assertEquals("Missing required string field 'id'", readMessage(action("show_entity")));
        }
    }

    @Nested
    class Malformed {

        @Test
        void unknownAction() {
            assertEquals("Unknown hover event action 'show_potato'", readMessage(action("show_potato")));
        }

        @Test
        void missingAction() {
            assertEquals("Missing required string field 'action'",
                readMessage(NbtMap.builder().putString("value", "hi").build()));
        }

        @Test
        @DisplayName("show_achievement is an adventure-only leftover with no nbt form, in either direction")
        void showAchievement() {
            assertEquals("A show_achievement hover event has no nbt form",
                readMessage(NbtMap.builder()
                    .putString("action", "show_achievement")
                    .putString("value", "achievement.openInventory")
                    .build()));

            final NbtSerializationException thrown = assertThrows(NbtSerializationException.class,
                () -> HOVER.serialize(HoverEvent.showAchievement("achievement.openInventory")));
            assertEquals("A show_achievement hover event has no nbt form", thrown.getMessage());
        }
    }
}
