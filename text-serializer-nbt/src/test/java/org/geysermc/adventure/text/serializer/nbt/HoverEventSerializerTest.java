package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.List;
import java.util.UUID;

public class HoverEventSerializerTest {
    static final List<Arguments> HOVER_EVENTS = List.of(
        Arguments.arguments(
            hoverEvent("show_text").putString("value", "test 1 2 3"),
            HoverEvent.showText(Component.text("test 1 2 3"))
        ),
        Arguments.arguments(
            hoverEvent("show_item").putString("id", "minecraft:stone"),
            HoverEvent.showItem(Key.key("stone"), 1)
        ),
        Arguments.arguments(
            hoverEvent("show_item")
                .putString("id", "minecraft:diamond")
                .putInt("count", 32),
            HoverEvent.showItem(Key.key("diamond"), 32)
        ), // FIXME when binary tag holder impl (data components)
        Arguments.arguments(
            hoverEvent("show_entity")
                .putString("id", "minecraft:zombie")
                .putIntArray("uuid", new int[]{-1672631430,-75086590,-1822946037,1701451835}),
            HoverEvent.showEntity(Key.key("zombie"), new UUID(-7183897285891832574L, -7829493609586354117L))
        ),
        Arguments.arguments(
            hoverEvent("show_entity")
                .putString("name", "eclipseisoffline")
                .putString("id", "minecraft:player")
                .putIntArray("uuid", new int[]{-476674174, 1357660845, -1694618559, 456679868}),
            HoverEvent.showEntity(Key.key("player"), new UUID(-2047299986820152659L, -7278331289642966596L), Component.text("eclipseisoffline"))
        )
    );

    @ParameterizedTest
    @FieldSource("HOVER_EVENTS")
    void testDeserialize(NbtMapBuilder map, HoverEvent<?> result) {
        Assertions.assertEquals(result, HoverEventSerializerImpl.deserialize(map.build(), NbtComponentSerializer.nbt()));
    }

    @ParameterizedTest
    @FieldSource("HOVER_EVENTS")
    void testSerialize(NbtMapBuilder result, HoverEvent<?> event) {
        Assertions.assertEquals(result.build(), HoverEventSerializerImpl.serialize(event, NbtComponentSerializer.nbt()));
    }

    @Test
    void testSupportedEvents() {
        HoverEvent.Action.NAMES.values().forEach(action -> Assertions.assertTrue(HoverEventSerializerImpl.SUPPORTED_EVENTS.contains(action),
            "HoverEventSerializerImpl must support hover event: " + action.name()));
    }

    private static NbtMapBuilder hoverEvent(String action) {
        return NbtMap.builder().putString("action", action);
    }
}
