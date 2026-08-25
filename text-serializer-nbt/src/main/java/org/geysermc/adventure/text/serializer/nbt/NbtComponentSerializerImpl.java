package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;

import java.util.List;
import java.util.Optional;

final class NbtComponentSerializerImpl implements NbtComponentSerializer {
    static final NbtComponentSerializerImpl INSTANCE = new NbtComponentSerializerImpl();

    @Override
    public Component deserialize(Object input) {
        return switch (input) {
            case String simpleText -> Component.text(simpleText);
            // TODO does this properly apply the style of the first component in the list?
            case NbtList<?> list -> Component.join(JoinConfiguration.noSeparators(), HeterogeneousNbtList.tryUnwrap(list).stream().map(this::deserialize).toList());
            case NbtMap map -> {
                Component baseComponent = deserializeFuzzyComponent(map);
                // TODO possible performance issue
                baseComponent = baseComponent.children(HeterogeneousNbtList.getList(map, "extra").stream().map(this::deserialize).toList());
                // TODO possible performance issue
                yield baseComponent.style(StyleSerializerImpl.deserialize(map, this));
            }
            default -> throw new IllegalArgumentException("Don't know how to parse component: " + input);
        };
    }

    @Override
    public Object serialize(Component component) {
        if (component instanceof TextComponent text && component.children().isEmpty() && !component.hasStyling()) {
            return text.content();
        }

        NbtMapBuilder builder = NbtMap.builder();

        switch (component) {
            case TextComponent text -> builder.putString("text", text.content());
            case TranslatableComponent translatable -> {
                builder.putString("translate", translatable.key());
                NbtSerializationUtil.checkNonNull(translatable.fallback(), fallback -> builder.putString("fallback", fallback));

                NbtList<?> arguments = translatable.arguments().stream().map(this::serializeTranslationArgument).collect(HeterogeneousNbtList.collector());
                if (!arguments.isEmpty()) {
                    builder.put("with", arguments);
                }
            }
            case KeybindComponent keybind -> builder.putString("keybind", keybind.keybind());
            case ScoreComponent score -> builder.putCompound("score", NbtMap.builder()
                .putString("name", score.name())
                .putString("objective", score.objective())
                .build()
            );
            case SelectorComponent selector -> {
                builder.putString("selector", selector.pattern());
                NbtSerializationUtil.checkNonNull(selector.separator(), separator -> builder.put("separator", serialize(separator)));
            }
            case NBTComponent<?> nbtComponent -> serializeNbtContentsComponent(builder, nbtComponent);
            case ObjectComponent objectComponent -> serializeObjectComponent(builder, objectComponent);
        }

        if (!component.children().isEmpty()) {
            builder.put("extra", component.children().stream().map(this::serialize).collect(HeterogeneousNbtList.collector()));
        }
        StyleSerializerImpl.serialize(builder, component.style(), this);

        return builder.build();
    }

    private Component deserializeFuzzyComponent(NbtMap map) {
        String text = map.getString("text", null);
        if (text != null) {
            return Component.text(text);
        }
        String translate = map.getString("translate", null);
        if (translate != null) {
            String fallback = map.getString("fallback", null);
            List<TranslationArgument> translationArguments = HeterogeneousNbtList.getList(map, "with").stream().map(this::deserializeTranslationArgument).toList();
            return Component.translatable(translate, fallback, translationArguments);
        }
        String keybind = map.getString("keybind", null);
        if (keybind != null) {
            return Component.keybind(keybind);
        }
        NbtMap score = map.getCompound("score", null);
        if (score != null) {
            return Component.score(map.getString("name", null), map.getString("objective", null));
        }
        String nbtPath = map.getString("nbt", null);
        if (nbtPath != null) {
            return deserializeFuzzyNbtContentsComponent(map, nbtPath);
        }
        return deserializeFuzzyObjectComponent(map).orElseThrow(() -> new IllegalArgumentException("Don't know how to parse component: " + map));
    }

    private TranslationArgument deserializeTranslationArgument(Object object) {
        return switch (object) {
            case Boolean bool -> TranslationArgument.bool(bool);
            case Number number -> TranslationArgument.numeric(number);
            default -> TranslationArgument.component(deserialize(object));
        };
    }

    private Object serializeTranslationArgument(TranslationArgument argument) {
        if (argument.value() instanceof Component component) {
            return serialize(component);
        }
        return argument.value();
    }

    private NBTComponent<?> deserializeFuzzyNbtContentsComponent(NbtMap map, String nbtPath) {
        // TODO maybe make this cleaner
        boolean interpret = map.getBoolean("interpret", false);
        boolean plain = map.getBoolean("plain", false);
        Component separator = deserializeOrNull(map.get("separator"));

        String entity = map.getString("entity", null);
        if (entity != null) {
            return Component.entityNBT(builder -> builder
                .nbtPath(nbtPath)
                .interpret(interpret)
                .plain(plain)
                .separator(separator)
                .selector(entity));
        }
        String block = map.getString("block", null);
        if (block != null) {
            return Component.blockNBT(builder -> builder
                .nbtPath(nbtPath)
                .interpret(interpret)
                .plain(plain)
                .separator(separator)
                .pos(BlockNBTComponent.Pos.fromString(block)));
        }
        String storage = map.getString("storage", null);
        if (storage != null) {
            return Component.storageNBT(builder -> builder
                .nbtPath(nbtPath)
                .interpret(interpret)
                .plain(plain)
                .separator(separator)
                .storage(Key.key(storage)));
        }
        throw new IllegalArgumentException("Don't know how to parse NBT component: " + map);
    }

    private void serializeNbtContentsComponent(NbtMapBuilder builder, NBTComponent<?> component) {
        builder.putString("nbt", component.nbtPath());
        NbtSerializationUtil.putIfTrue(builder, "interpret", component.interpret());
        NbtSerializationUtil.putIfTrue(builder, "plain", component.plain());
        NbtSerializationUtil.checkNonNull(component.separator(), separator -> builder.put("separator", serialize(separator)));

        switch (component) {
            case EntityNBTComponent entity -> builder.putString("entity", entity.selector());
            case BlockNBTComponent block -> builder.putString("block", block.pos().asString());
            case StorageNBTComponent storage -> builder.putString("storage", storage.storage().asString());
        }
    }

    private Optional<ObjectComponent> deserializeFuzzyObjectComponent(NbtMap map) {
        Component fallback = deserializeOrNull(map.get("fallback"));
        String sprite = map.getString("sprite", null);
        if (sprite != null) {
            String atlas = map.getString("atlas", null);
            return Optional.of(Component.object(builder -> builder
                .contents(ObjectContents.sprite(atlas == null ? SpriteObjectContents.DEFAULT_ATLAS : Key.key(atlas), Key.key(sprite)))
                .fallback(fallback)));
        }
        NbtMap player = map.getCompound("player", null);
        if (player != null) {
            PlayerHeadObjectContents.Builder contents = ResolvableProfileSerializerImpl.deserialize(player)
                .hat(map.getBoolean("hat", true));
            return Optional.of(Component.object(builder -> builder
                .contents(contents.build())
                .fallback(fallback)));
        }
        return Optional.empty();
    }

    private void serializeObjectComponent(NbtMapBuilder builder, ObjectComponent component) {
        NbtSerializationUtil.checkNonNull(component.fallback(), fallback -> builder.put("fallback", serialize(fallback)));

        switch (component.contents()) {
            case SpriteObjectContents sprite -> {
                if (!sprite.atlas().equals(SpriteObjectContents.DEFAULT_ATLAS)) {
                    builder.putString("atlas", sprite.atlas().asString());
                }
                builder.putString("sprite", sprite.sprite().asString());
            }
            case PlayerHeadObjectContents playerHead -> {
                builder.putCompound("player", ResolvableProfileSerializerImpl.serialize(playerHead));
                NbtSerializationUtil.putIfFalse(builder, "hat", playerHead.hat());
            }
        }
    }
}
