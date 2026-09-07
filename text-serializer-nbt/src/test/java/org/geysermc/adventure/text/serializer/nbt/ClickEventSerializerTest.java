package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickEvent;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.List;

public class ClickEventSerializerTest {
    static final List<Arguments> CLICK_EVENTS = List.of(
        Arguments.arguments(
            clickEvent("open_url").putString("url", "https://www.youtube.com/watch?v=dQw4w9WgXcQ"),
            ClickEvent.openUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        ),
        Arguments.arguments(
            clickEvent("open_file").putString("path", "/home/eclipse/Documents/Code/Java/Minecraft/Geyser/Geyser/hidden-cheat-codes"),
            ClickEvent.openFile("/home/eclipse/Documents/Code/Java/Minecraft/Geyser/Geyser/hidden-cheat-codes")
        ),
        Arguments.arguments(
            clickEvent("run_command").putString("command", "give eclipseisoffline diamond 64"),
            ClickEvent.runCommand("give eclipseisoffline diamond 64")
        ),
        Arguments.arguments(
            clickEvent("suggest_command").putString("command", "clear AuriiU diamond 64"),
            ClickEvent.suggestCommand("clear AuriiU diamond 64")
        ),
        Arguments.arguments(
            clickEvent("change_page").putInt("page", 5),
            ClickEvent.changePage(5)
        ),
        Arguments.arguments(
            clickEvent("copy_to_clipboard").putString("value", "aGVsbG8gZnJvbSBlY2xpcHNlIDop"),
            ClickEvent.copyToClipboard("aGVsbG8gZnJvbSBlY2xpcHNlIDop")
        ),
        Arguments.arguments(
            clickEvent("show_dialog").putString("dialog", "geyser:login_form"),
            ClickEvent.showDialog(new NbtDialog(Key.key("geyser", "login_form")))
        ),
        Arguments.arguments(
            clickEvent("show_dialog").putCompound("dialog", NbtMap.EMPTY),
            ClickEvent.showDialog(new NbtDialog(NbtMap.EMPTY))
        ),
        Arguments.arguments(
            clickEvent("custom").putString("id", "geyser:my_payload"),
            ClickEvent.custom(Key.key("geyser", "my_payload"))
        )
        // Payloads are not supported yet
        /*
        Arguments.arguments(
            clickEvent("custom")
                .putString("id", "geyser:complicated_payload")
                .putCompound("payload", NbtMap.builder()
                    .putString("complicated", "Very much")
                    .build()),
            ClickEvent.custom(Key.key("geyser", "complicated_payload"), new NbtBinaryTagHolder(NbtMap.builder()
                .putString("complicated", "Very much")
                .build()))
        )
         */
    );

    @ParameterizedTest
    @FieldSource("CLICK_EVENTS")
    void testDeserialize(NbtMapBuilder map, ClickEvent<?> result) {
        Assertions.assertEquals(result, ClickEventSerializerImpl.deserialize(map.build()));
    }

    @ParameterizedTest
    @FieldSource("CLICK_EVENTS")
    void testSerialize(NbtMapBuilder result, ClickEvent<?> event) {
        Assertions.assertEquals(result.build(), ClickEventSerializerImpl.serialize(event));
    }

    @Test
    void testDeserializeInvalidDialog() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ClickEventSerializerImpl.deserialize(NbtMap.builder()
            .putString("action", "show_dialog")
            .putInt("dialog", 12345)
            .build()), "decoding a dialog that is not a string or NbtMap should throw an IllegalArgumentException");
    }

    @Test
    void testSerializeInvalidDialog() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ClickEventSerializerImpl.serialize(ClickEvent.showDialog(new DialogLike() {})),
            "encoding a dialog that is not an NbtDialog should throw an IllegalArgumentException");
    }

    private static NbtMapBuilder clickEvent(String action) {
        return NbtMap.builder().putString("action", action);
    }
}
