package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertReads;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertRoundTrips;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertWrites;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The {@code click_event} compound. Since 1.21.5 each action carries its own named field rather than a
 * shared {@code value}, so every action has its own shape to get right.
 */
class ClickEventTest {

    private static NbtMap action(final String name) {
        return NbtMap.builder().putString("action", name).build();
    }

    private static String messageOf(final NbtMap tag) {
        return assertThrows(NbtSerializationException.class, () -> ClickEventSerializer.deserialize(tag)).getMessage();
    }

    /**
     * A literal that carries the event in its style, so that the same shape is exercised through the
     * public serializer and not only through the package-private one.
     */
    private static Component styled(final ClickEvent<?> event) {
        return Component.text("click me").clickEvent(event);
    }

    private static NbtMap styledTag(final NbtMap event) {
        return NbtMap.builder()
            .putString("text", "click me")
            .putCompound("click_event", event)
            .build();
    }

    @Nested
    @DisplayName("the actions that carry a single string, each under its own field name")
    class TextActions {

        @Test
        void openUrl() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "open_url")
                .putString("url", "https://example.com/a?b=c")
                .build();

            assertEquals(expected, ClickEventSerializer.serialize(ClickEvent.openUrl("https://example.com/a?b=c")));
            assertEquals(ClickEvent.openUrl("https://example.com/a?b=c"), ClickEventSerializer.deserialize(expected));
        }

        @Test
        @DisplayName("open_file uses path, not url")
        void openFile() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "open_file")
                .putString("path", "/home/steve/screenshot.png")
                .build();

            assertEquals(expected, ClickEventSerializer.serialize(ClickEvent.openFile("/home/steve/screenshot.png")));
            assertEquals(ClickEvent.openFile("/home/steve/screenshot.png"), ClickEventSerializer.deserialize(expected));
        }

        @Test
        void runCommand() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "run_command")
                .putString("command", "/say hi")
                .build();

            assertEquals(expected, ClickEventSerializer.serialize(ClickEvent.runCommand("/say hi")));
            assertEquals(ClickEvent.runCommand("/say hi"), ClickEventSerializer.deserialize(expected));
        }

        @Test
        @DisplayName("suggest_command shares the command field with run_command, and only the action tells them apart")
        void suggestCommand() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "suggest_command")
                .putString("command", "/tp ")
                .build();

            assertEquals(expected, ClickEventSerializer.serialize(ClickEvent.suggestCommand("/tp ")));
            assertEquals(ClickEvent.suggestCommand("/tp "), ClickEventSerializer.deserialize(expected));
        }

        @Test
        @DisplayName("copy_to_clipboard is the one action that really does use value")
        void copyToClipboard() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "copy_to_clipboard")
                .putString("value", "some text")
                .build();

            assertEquals(expected, ClickEventSerializer.serialize(ClickEvent.copyToClipboard("some text")));
            assertEquals(ClickEvent.copyToClipboard("some text"), ClickEventSerializer.deserialize(expected));
        }

        @Test
        @DisplayName("the pre-1.21.5 shared value field is not read for the other actions")
        void legacyValueFieldIsNotAccepted() {
            assertEquals("Missing required string field 'url'",
                messageOf(NbtMap.builder()
                    .putString("action", "open_url")
                    .putString("value", "https://example.com")
                    .build()));
            assertEquals("Missing required string field 'command'",
                messageOf(NbtMap.builder()
                    .putString("action", "run_command")
                    .putString("value", "/say hi")
                    .build()));
        }
    }

    @Nested
    class ChangePage {

        @Test
        @DisplayName("page is an int, not a string")
        void page() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "change_page")
                .putInt("page", 3)
                .build();

            assertEquals(expected, ClickEventSerializer.serialize(ClickEvent.changePage(3)));
            assertEquals(ClickEvent.changePage(3), ClickEventSerializer.deserialize(expected));
        }

        @Test
        @DisplayName("page one is the smallest vanilla's ExtraCodecs.POSITIVE_INT allows")
        void pageOne() {
            assertEquals(ClickEvent.changePage(1), ClickEventSerializer.deserialize(NbtMap.builder()
                .putString("action", "change_page")
                .putInt("page", 1)
                .build()));
        }

        @Test
        void zeroIsRejected() {
            assertEquals("A change_page click event needs a page of at least 1, got 0",
                messageOf(NbtMap.builder().putString("action", "change_page").putInt("page", 0).build()));
        }

        @Test
        void negativeIsRejected() {
            assertEquals("A change_page click event needs a page of at least 1, got -7",
                messageOf(NbtMap.builder().putString("action", "change_page").putInt("page", -7).build()));
        }

        @Test
        void missingPageIsRejected() {
            assertEquals("Missing required int field 'page'", messageOf(action("change_page")));
        }

        @Test
        void aStringIsNotAPage() {
            assertEquals("Expected field 'page' to be a number, got String",
                messageOf(NbtMap.builder().putString("action", "change_page").putString("page", "3").build()));
        }
    }

    @Nested
    @DisplayName("custom, whose payload is opaque and must never be interpreted")
    class Custom {

        private static final Key ID = Key.key("mypack", "open_shop");

        /**
         * Deliberately full of keys a component reader would recognise, plus nesting, so that anything
         * that tried to reinterpret the payload as a component would be caught.
         */
        private static final NbtMap PAYLOAD = NbtMap.builder()
            .putString("type", "text")
            .putString("text", "not a component")
            .putString("translate", "not.a.translation.key")
            .putBoolean("bold", true)
            .putInt("count", 7)
            .putCompound("nested", NbtMap.builder()
                .putString("text", "still not a component")
                .putLong("stamp", 1234567890123L)
                .build())
            .putList("list", NbtType.STRING, List.of("a", "b"))
            .build();

        private static NbtMap withPayload() {
            return NbtMap.builder()
                .putString("action", "custom")
                .putString("id", "mypack:open_shop")
                .putCompound("payload", PAYLOAD)
                .build();
        }

        @Test
        void withoutAPayload() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "custom")
                .putString("id", "mypack:open_shop")
                .build();

            assertEquals(expected, ClickEventSerializer.serialize(ClickEvent.custom(ID)));
            assertEquals(ClickEvent.custom(ID), ClickEventSerializer.deserialize(expected));

            final ClickEvent<?> read = ClickEventSerializer.deserialize(expected);
            assertNull(((ClickEvent.Payload.Custom) read.payload()).nbt());
        }

        @Test
        @DisplayName("a payload is carried as an opaque tag, component-shaped keys and all")
        void payloadIsCarriedUntouched() {
            final ClickEvent<?> read = ClickEventSerializer.deserialize(withPayload());
            assertEquals(ID, ((ClickEvent.Payload.Custom) read.payload()).key());
            assertEquals(PAYLOAD, ((ClickEvent.Payload.Custom) read.payload()).nbt().get(SnbtCodec.CODEC));
        }

        @Test
        @DisplayName("the payload survives the trip through the BinaryTagHolder's snbt string")
        void payloadRoundTripsThroughTheHolder() {
            assertEquals(withPayload(), ClickEventSerializer.serialize(ClickEventSerializer.deserialize(withPayload())));
        }

        @Test
        void writesAHolderBuiltElsewhere() {
            final ClickEvent<?> event = ClickEvent.custom(ID, BinaryTagHolder.encode(PAYLOAD, SnbtCodec.CODEC));
            assertEquals(withPayload(), ClickEventSerializer.serialize(event));
        }

        @Test
        void missingIdIsRejected() {
            assertEquals("Missing required string field 'id'", messageOf(action("custom")));
        }
    }

    @Nested
    @DisplayName("show_dialog, which adventure can only model as a registry reference")
    class ShowDialog {

        @Test
        void registryReferenceRoundTrips() {
            final NbtMap expected = NbtMap.builder()
                .putString("action", "show_dialog")
                .putString("dialog", "minecraft:quick_actions")
                .build();
            final ClickEvent<?> event =
                ClickEvent.showDialog(new ClickEventSerializer.DialogReference(Key.key("minecraft:quick_actions")));

            assertEquals(event, ClickEventSerializer.deserialize(expected));
            assertEquals(expected, ClickEventSerializer.serialize(event));
            assertEquals(expected, ClickEventSerializer.serialize(ClickEventSerializer.deserialize(expected)));
        }

        @Test
        @DisplayName("an inline dialog is rejected: DialogLike is an empty marker with nothing to build from")
        void inlineDialogIsRejected() {
            assertEquals("An inline dialog of a show_dialog click event cannot be read: "
                    + "adventure has no api to build one, only a registry reference is supported",
                messageOf(NbtMap.builder()
                    .putString("action", "show_dialog")
                    .putCompound("dialog", NbtMap.builder()
                        .putString("type", "minecraft:notice")
                        .putString("title", "hello")
                        .build())
                    .build()));
        }

        @Test
        void missingDialogIsRejected() {
            assertEquals("Expected field 'dialog' to be a string or a compound, got nothing",
                messageOf(action("show_dialog")));
        }

        @Test
        void aNumberIsNotADialog() {
            assertEquals("Expected field 'dialog' to be a string or a compound, got Integer",
                messageOf(NbtMap.builder().putString("action", "show_dialog").putInt("dialog", 1).build()));
        }

        @Test
        @DisplayName("a dialog that carries no key cannot be written")
        void keylessDialogIsRejectedOnWrite() {
            final DialogLike keyless = new DialogLike() {
            };
            final NbtSerializationException thrown = assertThrows(NbtSerializationException.class,
                () -> ClickEventSerializer.serialize(ClickEvent.showDialog(keyless)));
            assertEquals("A show_dialog click event can only be written as a registry reference, "
                + "and this dialog carries no key: " + keyless.getClass().getName(), thrown.getMessage());
        }
    }

    @Nested
    class Malformed {

        @Test
        void unknownAction() {
            assertEquals("Unknown click event action 'open_portal'", messageOf(action("open_portal")));
        }

        @Test
        void missingAction() {
            assertEquals("Missing required string field 'action'",
                messageOf(NbtMap.builder().putString("url", "https://example.com").build()));
        }

        @Test
        void missingUrl() {
            assertEquals("Missing required string field 'url'", messageOf(action("open_url")));
        }

        @Test
        void missingPath() {
            assertEquals("Missing required string field 'path'", messageOf(action("open_file")));
        }

        @Test
        void missingCommand() {
            assertEquals("Missing required string field 'command'", messageOf(action("run_command")));
            assertEquals("Missing required string field 'command'", messageOf(action("suggest_command")));
        }

        @Test
        void missingClipboardValue() {
            assertEquals("Missing required string field 'value'", messageOf(action("copy_to_clipboard")));
        }
    }

    @Nested
    @DisplayName("the same events through a full component, under the style's click_event key")
    class ThroughAComponent {

        @Test
        void runCommand() {
            final NbtMap expected = styledTag(NbtMap.builder()
                .putString("action", "run_command")
                .putString("command", "/say hi")
                .build());

            assertWrites(styled(ClickEvent.runCommand("/say hi")), expected);
            assertReads(expected, styled(ClickEvent.runCommand("/say hi")));
            assertRoundTrips(styled(ClickEvent.runCommand("/say hi")));
        }

        @Test
        void openUrl() {
            final NbtMap expected = styledTag(NbtMap.builder()
                .putString("action", "open_url")
                .putString("url", "https://example.com")
                .build());

            assertWrites(styled(ClickEvent.openUrl("https://example.com")), expected);
            assertReads(expected, styled(ClickEvent.openUrl("https://example.com")));
            assertRoundTrips(styled(ClickEvent.openUrl("https://example.com")));
        }

        @Test
        void changePage() {
            final NbtMap expected = styledTag(NbtMap.builder()
                .putString("action", "change_page")
                .putInt("page", 12)
                .build());

            assertWrites(styled(ClickEvent.changePage(12)), expected);
            assertReads(expected, styled(ClickEvent.changePage(12)));
            assertRoundTrips(styled(ClickEvent.changePage(12)));
        }

        @Test
        void customWithAPayload() {
            final NbtMap payload = NbtMap.builder()
                .putString("text", "opaque")
                .putInt("slot", 4)
                .build();
            final ClickEvent<?> event =
                ClickEvent.custom(Key.key("mypack", "open_shop"), BinaryTagHolder.encode(payload, SnbtCodec.CODEC));
            final NbtMap expected = styledTag(NbtMap.builder()
                .putString("action", "custom")
                .putString("id", "mypack:open_shop")
                .putCompound("payload", payload)
                .build());

            assertWrites(styled(event), expected);
            assertReads(expected, styled(event));
            assertRoundTrips(styled(event));
        }

        @Test
        void showDialog() {
            final ClickEvent<?> event =
                ClickEvent.showDialog(new ClickEventSerializer.DialogReference(Key.key("minecraft:quick_actions")));
            final NbtMap expected = styledTag(NbtMap.builder()
                .putString("action", "show_dialog")
                .putString("dialog", "minecraft:quick_actions")
                .build());

            assertWrites(styled(event), expected);
            assertReads(expected, styled(event));
            assertRoundTrips(styled(event));
        }

        @Test
        @DisplayName("a component with no click event writes no click_event key")
        void absentEventWritesNothing() {
            final NbtMap written = (NbtMap) NbtComponentAssertions.NBT.serialize(
                Component.text("plain", NamedTextColor.RED));
            assertEquals(NbtMap.builder().putString("text", "plain").putString("color", "red").build(), written);
            assertFalse(written.containsKey("click_event"));
        }
    }
}
