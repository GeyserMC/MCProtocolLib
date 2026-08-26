package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.object.ObjectContents;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NbtComponentSerializerTest {
    private static final NbtMapBuilder TRANSLATE_COMPONENT_TEST_CASE = NbtMap.builder()
        .putString("translate", "message.about.cats")
        .putString("fallback", "another.message");

    static {
        // Not a fan, Cloudburts/NBT
        TRANSLATE_COMPONENT_TEST_CASE.put("with", HeterogeneousNbtList.of("argument 1", NbtMap.builder()
            .putString("text", "argument 2")
            .putString("color", "gray")
            .build()));
    }

    static final List<Arguments> GENERAL_COMPONENTS = List.of(
        Arguments.arguments(
            "hiiiiiiiiiiiiii I luv cats",
            Component.text("hiiiiiiiiiiiiii I luv cats")
        ),
        Arguments.arguments(
            TRANSLATE_COMPONENT_TEST_CASE,
            Component.translatable("message.about.cats", "another.message")
                .arguments(Component.text("argument 1"), Component.text("argument 2").color(NamedTextColor.GRAY))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("keybind", "key.left")
                .putBoolean("italic", false),
            Component.keybind("key.left").decoration(TextDecoration.ITALIC, false)
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putCompound("score", NbtMap.builder()
                    .putString("name", "eclipseisoffline")
                    .putString("objective", "diamonds_mined")
                    .build()),
            Component.score("eclipseisoffline", "diamonds_mined")
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("selector", "@a"),
            Component.selector("@a")
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("selector", "@a[tag=cool_people]")
                .putString("separator", ".-."),
            Component.selector("@a[tag=cool_people]", Component.text(".-."))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("nbt", "SelectedItem.id")
                .putString("entity", "@s"),
            Component.entityNBT("SelectedItem.id", "@s")
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("sprite", "minecraft:emerald"),
            Component.object(ObjectContents.sprite(Key.key("emerald")))
        )
    );
    static final List<Arguments> TRANSLATION_ARGUMENTS = List.of(
        Arguments.arguments(
            true,
            TranslationArgument.bool(true)
        ),
        Arguments.arguments(
            false,
            TranslationArgument.bool(false)
        ),
        Arguments.arguments(
            5.2,
            TranslationArgument.numeric(5.2)
        ),
        Arguments.arguments(
            -100L,
            TranslationArgument.numeric(-100L)
        ),
        Arguments.arguments(
            (short) 345,
            TranslationArgument.numeric((short) 345)
        ),
        // Note that technically, in vanilla, this is stored as a primitive string, not an actual text component: https://mcsrc.dev/2/26.2/net/minecraft/network/chat/contents/TranslatableContents#L61-63
        // It doesn't matter much since it's basically the same anyway
        Arguments.arguments(
            "hello 1 2 3",
            TranslationArgument.component(Component.text("hello 1 2 3"))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("text", "hello")
                .putList("extra", NbtType.STRING, "2", "4")
                .build(),
            TranslationArgument.component(Component.text("hello").append(Component.text("2")).append(Component.text("4")))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("translate", "string.3")
                .putList("extra", NbtType.COMPOUND, NbtMap.builder()
                    .putString("text", "hello")
                    .putBoolean("bold", true)
                    .build())
                .putString("color", "red")
                .build(),
            TranslationArgument.component(Component.translatable("string.3")
                .append(Component.text("hello").decorate(TextDecoration.BOLD))
                .color(NamedTextColor.RED))
        )
    );
    static final List<Arguments> NBT_COMPONENTS = List.of(
        Arguments.arguments(
            NbtMap.builder()
                .putString("text", "this is definitely not an nbt contents component"),
            Optional.empty()
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("nbt", "Inventory[0].components.\"minecraft:enchantments\"")
                .putString("entity", "@s"),
            Optional.of(Component.entityNBT("Inventory[0].components.\"minecraft:enchantments\"", "@s"))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("nbt", "my_custom.storage_key")
                .putBoolean("interpret", true)
                .putString("storage", "geyser:custom_storage"),
            Optional.of(Component.storageNBT("my_custom.storage_key", Key.key("geyser", "custom_storage")).interpret(true))
        ),
        /*Arguments.arguments(
            NbtMap.builder()
                .putString("nbt", "Items[0]")
                .putBoolean("plain", true)
                .putString("block", "~ ~5 ~"),
            Optional.of(Component.blockNBT("Items[0]", BlockNBTComponent.Pos.fromString("~ ~5 ~")).plain(true))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("nbt", "Items[5]")
                .putString("block", "^ ^ ^15"),
            Optional.of(Component.blockNBT("Items[5]", BlockNBTComponent.Pos.fromString("^ ^ ^15")))
        ),*/ // FIXME enable when adventure fixes Pos.fromstring
        Arguments.arguments(
            NbtMap.builder()
                .putString("nbt", "Items[3]")
                .putString("separator", ".")
                .putString("block", "1 2 3"),
            Optional.of(Component.blockNBT("Items[3]", BlockNBTComponent.Pos.fromString("1 2 3")).separator(Component.text(".")))
        )
    );
    static final List<Arguments> OBJECT_COMPONENTS = List.of(
        Arguments.arguments(
            NbtMap.builder()
                .putString("text", "dummy test hello"),
            Optional.empty()
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("sprite", "geyser:gayser_logo"),
            Optional.of(Component.object(ObjectContents.sprite(Key.key("geyser", "gayser_logo"))))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putString("atlas", "minecraft:item")
                .putString("sprite", "geyser:gay_diamond")
                .putString("fallback", "a gay diamond alt text"),
            Optional.of(Component.object(ObjectContents.sprite(Key.key("item"), Key.key("geyser", "gay_diamond"))).fallback(Component.text("a gay diamond alt text")))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putCompound("player", NbtMap.builder()
                    .putString("name", "AuriiU")
                    .build()),
            Optional.of(Component.object(ObjectContents.playerHead("AuriiU")))
        ),
        Arguments.arguments(
            NbtMap.builder()
                .putCompound("player", NbtMap.builder()
                    .putIntArray("id", new int[]{568551383, 634342971, -1301730230, 805981675})
                    .build())
                .putBoolean("hat", false)
                .putCompound("fallback", NbtMap.builder()
                    .putString("keybind", "key.forward")
                    .putBoolean("strikethrough", true)
                    .build()),
            Optional.of(Component.object(ObjectContents.playerHead()
                .id(new UUID(2441909596714913339L, -5590888765258576405L))
                .hat(false)
                .build()).fallback(Component.keybind("key.forward").decorate(TextDecoration.STRIKETHROUGH)))
        )
    );

    private final NbtComponentSerializerImpl serializer = (NbtComponentSerializerImpl) NbtComponentSerializer.nbt();

    @ParameterizedTest
    @FieldSource("GENERAL_COMPONENTS")
    void testDeserialize(Object object, Component result) {
        if (object instanceof NbtMapBuilder builder) {
            object = builder.build();
        }
        Assertions.assertEquals(result, serializer.deserialize(object));
    }

    @ParameterizedTest
    @FieldSource("GENERAL_COMPONENTS")
    void testSerialize(Object result, Component component) {
        Assertions.assertEquals(result, serializer.serialize(component));
    }

    @ParameterizedTest
    @FieldSource("TRANSLATION_ARGUMENTS")
    void testDeserializeTranslationArgument(Object object, TranslationArgument result) {
        Assertions.assertEquals(result, serializer.deserializeTranslationArgument(object));
    }

    @ParameterizedTest
    @FieldSource("TRANSLATION_ARGUMENTS")
    void testSerializeTranslationArgument(Object result, TranslationArgument argument) {
        Assertions.assertEquals(result, serializer.serializeTranslationArgument(argument));
    }

    @ParameterizedTest
    @FieldSource("NBT_COMPONENTS")
    void testDeserializeFuzzyNbtContentsComponent(NbtMapBuilder map, Optional<NBTComponent<?>> result) {
        Assertions.assertEquals(result, serializer.deserializeFuzzyNbtContentsComponent(map.build()));
    }

    @ParameterizedTest
    @FieldSource("NBT_COMPONENTS")
    void testSerializeNbtContentsComponent(NbtMapBuilder result, Optional<NBTComponent<?>> component) {
        // If empty then this component isn't an NBT contents component (for deserializing test only), so skip
        if (component.isPresent()) {
            NbtMapBuilder builder = NbtMap.builder();
            serializer.serializeNbtContentsComponent(builder, component.get());
            Assertions.assertEquals(result.build(), builder.build());
        }
    }

    @ParameterizedTest
    @FieldSource("OBJECT_COMPONENTS")
    void testDeserializeFuzzyObjectComponent(NbtMapBuilder map, Optional<ObjectComponent> result) {
        Assertions.assertEquals(result, serializer.deserializeFuzzyObjectComponent(map.build()));
    }

    @ParameterizedTest
    @FieldSource("OBJECT_COMPONENTS")
    void testSerializeObjectComponent(NbtMapBuilder result, Optional<ObjectComponent> component) {
        // If empty then this component isn't an object component (for deserializing test only), so skip
        if (component.isPresent()) {
            NbtMapBuilder builder = NbtMap.builder();
            serializer.serializeObjectComponent(builder, component.get());
            Assertions.assertEquals(result.build(), builder.build());
        }
    }
}
