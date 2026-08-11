package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertReads;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertRoundTrips;
import static org.geysermc.mcprotocollib.text.serializer.nbt.NbtComponentAssertions.assertWrites;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The {@code object} component, whose {@code ObjectInfo} vanilla inlines into the component compound
 * rather than nesting under a field of its own.
 */
class ObjectComponentTest {

    private static final UUID STEVE_ID = UUID.fromString("00112233-4455-6677-8899-aabbccddeeff");
    private static final int[] STEVE_ID_INTS = {0x00112233, 0x44556677, 0x8899AABB, 0xCCDDEEFF};

    private static NbtMap write(final ObjectContents contents) {
        final NbtMapBuilder tag = NbtMap.builder();
        ObjectContentsSerializer.serialize(contents, tag, false);
        return tag.build();
    }

    private static NbtMap writeWithDiscriminator(final ObjectContents contents) {
        final NbtMapBuilder tag = NbtMap.builder();
        ObjectContentsSerializer.serialize(contents, tag, true);
        return tag.build();
    }

    private static String readMessage(final NbtMap tag) {
        return assertThrows(NbtSerializationException.class,
            () -> ObjectContentsSerializer.deserialize(tag)).getMessage();
    }

    @Nested
    class Sprite {

        private static final Key SPRITE = Key.key("minecraft:icon/heart");

        @Test
        @DisplayName("the default atlas is left out, as a default valued field always is")
        void defaultAtlasIsOmitted() {
            final SpriteObjectContents contents = ObjectContents.sprite(SPRITE);
            assertEquals(Key.key("minecraft:blocks"), SpriteObjectContents.DEFAULT_ATLAS);
            assertEquals(SpriteObjectContents.DEFAULT_ATLAS, contents.atlas());

            final NbtMap written = write(contents);
            assertEquals(NbtMap.builder()
                .putString("sprite", "minecraft:icon/heart")
                .build(), written);
            assertFalse(written.containsKey("atlas"));
        }

        @Test
        @DisplayName("an atlas that happens to equal the default is still left out")
        void explicitDefaultAtlasIsOmitted() {
            assertFalse(write(ObjectContents.sprite(SpriteObjectContents.DEFAULT_ATLAS, SPRITE)).containsKey("atlas"));
        }

        @Test
        void nonDefaultAtlasIsWritten() {
            final SpriteObjectContents contents = ObjectContents.sprite(Key.key("minecraft:gui"), SPRITE);
            assertEquals(NbtMap.builder()
                .putString("atlas", "minecraft:gui")
                .putString("sprite", "minecraft:icon/heart")
                .build(), write(contents));
        }

        @Test
        @DisplayName("an absent atlas reads as the default")
        void absentAtlasReadsAsTheDefault() {
            assertEquals(ObjectContents.sprite(SPRITE), ObjectContentsSerializer.deserialize(NbtMap.builder()
                .putString("object", "atlas")
                .putString("sprite", "minecraft:icon/heart")
                .build()));
        }

        @Test
        void spriteIsRequired() {
            assertEquals("Missing required string field 'sprite'",
                readMessage(NbtMap.builder().putString("object", "atlas").build()));
        }

        @Test
        void throughAComponent() {
            final Component component = Component.object(ObjectContents.sprite(Key.key("minecraft:gui"), SPRITE));
            final NbtMap expected = NbtMap.builder()
                .putString("atlas", "minecraft:gui")
                .putString("sprite", "minecraft:icon/heart")
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        void throughAStyledComponent() {
            final Component component = Component.object(ObjectContents.sprite(SPRITE)).color(NamedTextColor.RED);
            final NbtMap expected = NbtMap.builder()
                .putString("sprite", "minecraft:icon/heart")
                .putString("color", "red")
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }
    }

    @Nested
    class PlayerHead {

        private static PlayerHeadObjectContents fullProfile() {
            return ObjectContents.playerHead()
                .name("Steve")
                .id(STEVE_ID)
                .profileProperties(List.of(
                    PlayerHeadObjectContents.property("textures", "base64value"),
                    PlayerHeadObjectContents.property("cape", "othervalue", "asignature")))
                .build();
        }

        private static NbtMap fullProfileTag() {
            return NbtMap.builder()
                .putCompound("player", NbtMap.builder()
                    .putString("name", "Steve")
                    .putIntArray("id", STEVE_ID_INTS)
                    .putList("properties", NbtType.COMPOUND, List.of(
                        NbtMap.builder()
                            .putString("name", "textures")
                            .putString("value", "base64value")
                            .build(),
                        NbtMap.builder()
                            .putString("name", "cape")
                            .putString("value", "othervalue")
                            .putString("signature", "asignature")
                            .build()))
                    .build())
                .build();
        }

        @Test
        @DisplayName("the whole ResolvableProfile: name, uuid as four ints, and signed properties")
        void fullProfileRoundTrips() {
            assertEquals(fullProfileTag(), write(fullProfile()));
            assertEquals(fullProfile(), ObjectContentsSerializer.deserialize(fullProfileTag()));

            final Component component = Component.object(fullProfile());
            assertWrites(component, fullProfileTag());
            assertReads(fullProfileTag(), component);
            assertRoundTrips(component);
        }

        @Test
        @DisplayName("a name only profile writes only a name")
        void nameOnly() {
            assertEquals(NbtMap.builder()
                .putCompound("player", NbtMap.builder().putString("name", "Steve").build())
                .build(), write(ObjectContents.playerHead().name("Steve").build()));
        }

        @Test
        @DisplayName("a bare string is ResolvableProfile's shorthand for a name only profile")
        void bareStringShorthandIsRead() {
            final NbtMapBuilder tag = NbtMap.builder();
            tag.putString("object", "player");
            tag.put("player", "Steve");

            assertEquals(ObjectContents.playerHead().name("Steve").build(),
                ObjectContentsSerializer.deserialize(tag.build()));
        }

        @Test
        @DisplayName("the shorthand is only a read form; a profile is always written as a compound")
        void shorthandIsNeverWritten() {
            final NbtMapBuilder tag = NbtMap.builder();
            tag.putString("object", "player");
            tag.put("player", "Steve");

            assertEquals(NbtMap.builder()
                .putCompound("player", NbtMap.builder().putString("name", "Steve").build())
                .build(), write(ObjectContentsSerializer.deserialize(tag.build())));
        }

        @Test
        @DisplayName("hat is left out when it holds its default of true")
        void defaultHatIsOmitted() {
            assertEquals(true, PlayerHeadObjectContents.DEFAULT_HAT);

            final NbtMap written = write(ObjectContents.playerHead().name("Steve").hat(true).build());
            assertFalse(written.containsKey("hat"));
            assertEquals(NbtMap.builder()
                .putCompound("player", NbtMap.builder().putString("name", "Steve").build())
                .build(), written);
        }

        @Test
        void nonDefaultHatIsWritten() {
            final PlayerHeadObjectContents contents = ObjectContents.playerHead().name("Steve").hat(false).build();
            final NbtMap expected = NbtMap.builder()
                .putCompound("player", NbtMap.builder().putString("name", "Steve").build())
                .putBoolean("hat", false)
                .build();

            assertEquals(expected, write(contents));
            assertEquals(contents, ObjectContentsSerializer.deserialize(expected));
            assertRoundTrips(Component.object(contents));
        }

        @Test
        @DisplayName("an absent hat reads as true, not as false")
        void absentHatReadsAsTrue() {
            final NbtMap tag = NbtMap.builder()
                .putString("object", "player")
                .putCompound("player", NbtMap.builder().putString("name", "Steve").build())
                .build();

            assertEquals(true, ((PlayerHeadObjectContents) ObjectContentsSerializer.deserialize(tag)).hat());
        }

        @Test
        void missingProfileIsRejected() {
            assertEquals("Missing required field 'player'",
                readMessage(NbtMap.builder().putString("object", "player").build()));
        }

        @Test
        void aNumberIsNotAProfile() {
            assertEquals("Expected field 'player' to be a compound or a string, got Integer",
                readMessage(NbtMap.builder().putString("object", "player").putInt("player", 1).build()));
        }

        @Test
        void aProfilePropertyNeedsANameAndAValue() {
            assertEquals("Missing required string field 'value'",
                readMessage(NbtMap.builder()
                    .putString("object", "player")
                    .putCompound("player", NbtMap.builder()
                        .putList("properties", NbtType.COMPOUND, List.of(
                            NbtMap.builder().putString("name", "textures").build()))
                        .build())
                    .build()));
        }

        @Test
        void aProfilePropertyMustBeACompound() {
            assertEquals("Expected a profile property to be a compound, got String",
                readMessage(NbtMap.builder()
                    .putString("object", "player")
                    .putCompound("player", NbtMap.builder()
                        .putList("properties", NbtType.STRING, List.of("textures"))
                        .build())
                    .build()));
        }

        @Test
        void aUuidMustBeAnIntArray() {
            assertEquals("Expected field 'id' to be an int array, got String",
                readMessage(NbtMap.builder()
                    .putString("object", "player")
                    .putCompound("player", NbtMap.builder()
                        .putString("id", "00112233-4455-6677-8899-aabbccddeeff")
                        .build())
                    .build()));
        }

        @Test
        void aUuidMustBeFourInts() {
            assertEquals("A uuid must be four ints, got 2",
                readMessage(NbtMap.builder()
                    .putString("object", "player")
                    .putCompound("player", NbtMap.builder()
                        .putIntArray("id", new int[]{1, 2})
                        .build())
                    .build()));
        }
    }

    @Nested
    @DisplayName("fallback, which belongs to the component rather than to the object info")
    class Fallback {

        @Test
        void plainFallback() {
            final Component component = Component.object()
                .contents(ObjectContents.sprite(Key.key("minecraft:icon/heart")))
                .fallback(Component.text("<heart>"))
                .build();
            final NbtMap expected = NbtMap.builder()
                .putString("sprite", "minecraft:icon/heart")
                .putString("fallback", "<heart>")
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        @DisplayName("a styled fallback is a compound, like any other nested component")
        void styledFallback() {
            final Component component = Component.object()
                .contents(ObjectContents.playerHead().name("Steve").build())
                .fallback(Component.text("Steve", NamedTextColor.GRAY))
                .build();
            final NbtMap expected = NbtMap.builder()
                .putCompound("player", NbtMap.builder().putString("name", "Steve").build())
                .putCompound("fallback", NbtMap.builder()
                    .putString("text", "Steve")
                    .putString("color", "gray")
                    .build())
                .build();

            assertWrites(component, expected);
            assertReads(expected, component);
            assertRoundTrips(component);
        }

        @Test
        void noFallbackWritesNoKey() {
            final NbtMap written =
                (NbtMap) NbtComponentAssertions.NBT.serialize(Component.object(ObjectContents.sprite(Key.key("minecraft:icon/heart"))));
            assertFalse(written.containsKey("fallback"));
        }
    }

    @Nested
    @DisplayName("the object discriminator, which is left out by default and wins over the field shape on read")
    class Discriminator {

        @Test
        @DisplayName("nothing is written by default, matching vanilla's ObjectInfos encoder")
        void discriminatorOmittedByDefault() {
            assertFalse(write(ObjectContents.sprite(Key.key("minecraft:icon/heart"))).containsKey("object"));
            assertFalse(write(ObjectContents.playerHead().name("Steve").build()).containsKey("object"));
        }

        @Test
        @DisplayName("it is written when the serializer was asked to emit discriminators")
        void discriminatorEmittedOnRequest() {
            assertEquals("atlas",
                writeWithDiscriminator(ObjectContents.sprite(Key.key("minecraft:icon/heart"))).getString("object"));
            assertEquals("player",
                writeWithDiscriminator(ObjectContents.playerHead().name("Steve").build()).getString("object"));
        }

        @Test
        @DisplayName("without a discriminator the sprite field identifies a sprite, since atlas is optional")
        void inferredSprite() {
            assertEquals(ObjectContents.sprite(Key.key("minecraft:icon/heart")),
                ObjectContentsSerializer.deserialize(NbtMap.builder()
                    .putString("sprite", "minecraft:icon/heart")
                    .build()));
        }

        @Test
        void inferredPlayerHead() {
            assertEquals(ObjectContents.playerHead().name("Steve").build(),
                ObjectContentsSerializer.deserialize(NbtMap.builder()
                    .putCompound("player", NbtMap.builder().putString("name", "Steve").build())
                    .build()));
        }

        @Test
        @DisplayName("an inferred object component is read through the component serializer too")
        void inferredThroughAComponent() {
            assertReads(NbtMap.builder().putString("sprite", "minecraft:icon/heart").build(),
                Component.object(ObjectContents.sprite(Key.key("minecraft:icon/heart"))));
        }

        @Test
        @DisplayName("an explicit discriminator wins, even when the other variant's field is the one present")
        void discriminatorBeatsTheFieldShape() {
            assertEquals("Missing required string field 'sprite'",
                readMessage(NbtMap.builder()
                    .putString("object", "atlas")
                    .putCompound("player", NbtMap.builder().putString("name", "Steve").build())
                    .build()));
        }

        @Test
        void unknownDiscriminatorIsRejected() {
            assertEquals("Unknown object type 'banner_pattern'",
                readMessage(NbtMap.builder()
                    .putString("object", "banner_pattern")
                    .putString("sprite", "minecraft:icon/heart")
                    .build()));
        }

        @Test
        void neitherFieldIsRejected() {
            assertEquals("An object component has no sprite or player field: []",
                readMessage(NbtMap.EMPTY));
            assertEquals("An object component has no sprite or player field: [hat]",
                readMessage(NbtMap.builder().putBoolean("hat", true).build()));
        }
    }
}
