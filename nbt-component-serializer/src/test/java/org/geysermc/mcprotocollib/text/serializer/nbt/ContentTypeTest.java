package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.cloudburstmc.nbt.NbtMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.NBT;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertReads;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertRoundTrips;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertWrites;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContentTypeTest {

    @Nested
    class Keybind {

        @Test
        void keybind() {
            final Component component = Component.keybind("key.jump");
            final NbtMap expected = NbtMap.builder().putString("keybind", "key.jump").build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        void keybindWithStyle() {
            final Component component = Component.keybind("key.sneak", NamedTextColor.RED);

            assertWrites(component, NbtMap.builder()
                .putString("keybind", "key.sneak")
                .putString("color", "red")
                .build());
            assertRoundTrips(component);
        }
    }

    @Nested
    @DisplayName("score, whose fields live in a nested score sub-compound")
    class Score {

        @Test
        void score() {
            final Component component = Component.score("Steve", "deaths");
            final NbtMap expected = NbtMap.builder()
                .putCompound("score", NbtMap.builder()
                    .putString("name", "Steve")
                    .putString("objective", "deaths")
                    .build())
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        @DisplayName("name and objective are not written at the top level")
        void fieldsAreNotFlattened() {
            final NbtMap written = (NbtMap) NBT.serialize(Component.score("*", "obj"));
            assertEquals(NbtMap.builder()
                .putCompound("score", NbtMap.builder()
                    .putString("name", "*")
                    .putString("objective", "obj")
                    .build())
                .build(), written);
            assertEquals(Set.of("score"), written.keySet());
        }
    }

    @Nested
    class Selector {

        @Test
        void selectorWithoutSeparator() {
            final Component component = Component.selector("@a[team=red]");
            final NbtMap expected = NbtMap.builder().putString("selector", "@a[team=red]").build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        void selectorWithSeparator() {
            final Component component = Component.selector("@e[type=pig]", Component.text(", "));
            final NbtMap expected = NbtMap.builder()
                .putString("selector", "@e[type=pig]")
                .putString("separator", ", ")
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        @DisplayName("a styled separator is a compound rather than a bare string")
        void selectorWithStyledSeparator() {
            final Component component = Component.selector("@a", Component.text(", ", NamedTextColor.GRAY));

            assertWrites(component, NbtMap.builder()
                .putString("selector", "@a")
                .putCompound("separator", NbtMap.builder()
                    .putString("text", ", ")
                    .putString("color", "gray")
                    .build())
                .build());
            assertRoundTrips(component);
        }
    }

    @Nested
    @DisplayName("nbt components and their three data sources")
    class Nbt {

        private static final BlockNBTComponent.Pos POS = BlockNBTComponent.Pos.fromString("1 2 3");

        @Test
        void blockSource() {
            final Component component = Component.blockNBT().nbtPath("Items").pos(POS).build();
            final NbtMap expected = NbtMap.builder()
                .putString("nbt", "Items")
                .putString("block", "1 2 3")
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        void entitySource() {
            final Component component = Component.entityNBT().nbtPath("Pos").selector("@s").build();
            final NbtMap expected = NbtMap.builder()
                .putString("nbt", "Pos")
                .putString("entity", "@s")
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        void storageSource() {
            final Component component = Component.storageNBT()
                .nbtPath("contents")
                .storage(Key.key("minecraft:my_storage"))
                .build();
            final NbtMap expected = NbtMap.builder()
                .putString("nbt", "contents")
                .putString("storage", "minecraft:my_storage")
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        @DisplayName("interpret is only written when it is on, matching vanilla's default of false")
        void interpret() {
            final Component interpreted = Component.storageNBT()
                .nbtPath("contents")
                .storage(Key.key("minecraft:my_storage"))
                .interpret(true)
                .build();
            final NbtMap expected = NbtMap.builder()
                .putString("nbt", "contents")
                .putBoolean("interpret", true)
                .putString("storage", "minecraft:my_storage")
                .build();

            assertWrites(interpreted, expected);
            assertReads(expected, interpreted);
            assertRoundTrips(interpreted);

            final NbtMap withoutInterpret = NbtMap.builder()
                .putString("nbt", "contents")
                .putString("storage", "minecraft:my_storage")
                .build();
            assertWrites(Component.storageNBT()
                .nbtPath("contents")
                .storage(Key.key("minecraft:my_storage"))
                .interpret(false)
                .build(), withoutInterpret);
        }

        @Test
        void plain() {
            final Component plain = Component.entityNBT().nbtPath("Health").selector("@s").plain(true).build();
            final NbtMap expected = NbtMap.builder()
                .putString("nbt", "Health")
                .putBoolean("plain", true)
                .putString("entity", "@s")
                .build();

            assertWrites(plain, expected);
            assertReads(expected, plain);
            assertRoundTrips(plain);
        }

        @Test
        void separator() {
            final Component component = Component.blockNBT()
                .nbtPath("Items")
                .pos(POS)
                .separator(Component.text(" | "))
                .build();
            final NbtMap expected = NbtMap.builder()
                .putString("nbt", "Items")
                .putString("separator", " | ")
                .putString("block", "1 2 3")
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        @DisplayName("an explicit source discriminator picks the data source, as in vanilla's NbtContents")
        void explicitSourceWins() {
            assertReads(NbtMap.builder()
                .putString("nbt", "Pos")
                .putString("source", "entity")
                .putString("entity", "@s")
                .putString("block", "1 2 3")
                .build(), Component.entityNBT().nbtPath("Pos").selector("@s").build());
        }

        @Test
        @DisplayName("interpret and plain are mutually exclusive")
        void interpretAndPlainTogetherIsRejected() {
            final NbtMap tag = NbtMap.builder()
                .putString("nbt", "Items")
                .putString("block", "1 2 3")
                .putBoolean("interpret", true)
                .putBoolean("plain", true)
                .build();

            final NbtSerializationException thrown =
                assertThrows(NbtSerializationException.class, () -> NBT.deserialize(tag));
            assertEquals("An nbt component cannot be both interpreted and plain", thrown.getMessage());
        }

        @Test
        @DisplayName("an nbt component with no recognisable source is rejected")
        void missingSourceIsRejected() {
            final NbtMap tag = NbtMap.builder().putString("nbt", "Items").build();
            final NbtSerializationException thrown =
                assertThrows(NbtSerializationException.class, () -> NBT.deserialize(tag));
            assertEquals("An nbt component needs a block, entity or storage source: [nbt]", thrown.getMessage());
        }
    }
}
