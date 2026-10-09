package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
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
import org.jetbrains.annotations.VisibleForTesting;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

final class NbtComponentSerializerImpl implements NbtComponentSerializer {
    static final NbtComponentSerializerImpl INSTANCE = new NbtComponentSerializerImpl();

    @Override
    public Component deserialize(Object input) {
        return switch (input) {
            case String simpleText -> Component.text(simpleText);
            // Vanilla takes the first element in the list and appends the rest of them to that
            // Vanilla also refuses to accept empty lists
            // See https://mcsrc.dev/2/26.2/net/minecraft/network/chat/ComponentSerialization#L108
            case NbtList<?> list -> HeterogeneousNbtList.tryUnwrap(list).stream()
                .map(this::deserialize)
                .map(Component::toBuilder)
                .reduce(ComponentBuilder::append)
                .map(ComponentBuilder::build)
                .orElseThrow(() -> new IllegalArgumentException("List of text components must have at least one element"));
            case NbtMap map -> deserializeFuzzyComponent(map)
                .append(HeterogeneousNbtList.getList(map, "extra").stream().map(this::deserialize).toList())
                .style(StyleSerializerImpl.deserialize(map, this))
                .build();
            default -> throw new IllegalArgumentException("Don't know how to parse component: " + input);
        };
    }

    @Override
    public Object serialize(Component component) {
        if (component instanceof TextComponent text && component.children().isEmpty() && !component.hasStyling()) {
            return text.content();
        }

        // switch case will intentionally break compile with new component types
        NbtMapBuilder builder = switch (component) {
            case TextComponent text -> NbtMap.builder().putString("text", text.content());
            case TranslatableComponent translatable -> {
                NbtMapBuilder map = NbtMap.builder();
                map.putString("translate", translatable.key());
                NbtUtil.checkNonNull(translatable.fallback(), fallback -> map.putString("fallback", fallback));

                NbtList<?> arguments = translatable.arguments().stream().map(this::serializeTranslationArgument).collect(HeterogeneousNbtList.collector());
                if (!arguments.isEmpty()) {
                    map.put("with", arguments);
                }
                yield map;
            }
            case KeybindComponent keybind -> NbtMap.builder().putString("keybind", keybind.keybind());
            case ScoreComponent score -> NbtMap.builder().putCompound("score", NbtMap.builder()
                .putString("name", score.name())
                .putString("objective", score.objective())
                .build()
            );
            case SelectorComponent selector -> {
                NbtMapBuilder map = NbtMap.builder();
                map.putString("selector", selector.pattern());
                NbtUtil.checkNonNull(selector.separator(), separator -> map.put("separator", serialize(separator)));
                yield map;
            }
            case NBTComponent<?> nbtComponent -> serializeNbtContentsComponent(nbtComponent);
            case ObjectComponent objectComponent -> serializeObjectComponent(objectComponent);
        };

        if (!component.children().isEmpty()) {
            builder.put("extra", component.children().stream().map(this::serialize).collect(HeterogeneousNbtList.collector()));
        }
        StyleSerializerImpl.serialize(builder, component.style(), this);

        return builder.build();
    }

    // When adding support for new components, make sure to follow the same parsing order as here:
    // https://mcsrc.dev/2/26.2/net/minecraft/network/chat/ComponentSerialization#L120-128
    private ComponentBuilder<?, ?> deserializeFuzzyComponent(NbtMap map) {
        String text = map.getString("text", null);
        if (text != null) {
            return Component.text().content(text);
        }
        String translate = map.getString("translate", null);
        if (translate != null) {
            String fallback = map.getString("fallback", null);
            List<TranslationArgument> translationArguments = HeterogeneousNbtList.getList(map, "with").stream().map(this::deserializeTranslationArgument).toList();
            return Component.translatable()
                .key(translate)
                .fallback(fallback)
                .arguments(translationArguments);
        }
        String keybind = map.getString("keybind", null);
        if (keybind != null) {
            return Component.keybind().keybind(keybind);
        }
        NbtMap score = map.getCompound("score", null);
        if (score != null) {
            return Component.score()
                .name(score.getString("name", null))
                .objective(score.getString("objective", null));
        }
        String selector = map.getString("selector", null);
        if (selector != null) {
            Component separator = deserializeOrNull(map.get("separator"));
            return Component.selector()
                .pattern(selector)
                .separator(separator);
        }
        return deserializeFuzzyNbtContentsComponent(map)
            .or(() -> deserializeFuzzyObjectComponent(map))
            .orElseThrow(() -> new IllegalArgumentException("Don't know how to parse component: " + map));
    }

    @VisibleForTesting
    TranslationArgument deserializeTranslationArgument(Object object) {
        return switch (object) {
            case Boolean bool -> TranslationArgument.bool(bool);
            case Number number -> TranslationArgument.numeric(number);
            default -> TranslationArgument.component(deserialize(object));
        };
    }

    @VisibleForTesting
    Object serializeTranslationArgument(TranslationArgument argument) {
        if (argument.value() instanceof Component component) {
            return serialize(component);
        }
        return argument.value();
    }

    @VisibleForTesting
    Optional<ComponentBuilder<?, ?>> deserializeFuzzyNbtContentsComponent(NbtMap map) {
        String nbtPath = map.getString("nbt", null);
        if (nbtPath == null) {
            return Optional.empty();
        }

        boolean interpret = map.getBoolean("interpret", false);
        boolean plain = map.getBoolean("plain", false);
        Component separator = deserializeOrNull(map.get("separator"));

        UnaryOperator<NBTComponentBuilder<?, ?>> baseBuilder = builder -> builder
            .nbtPath(nbtPath)
            .interpret(interpret)
            .plain(plain)
            .separator(separator);

        String entity = map.getString("entity", null);
        if (entity != null) {
            return Optional.of(baseBuilder.apply(Component.entityNBT().selector(entity)));
        }
        String block = map.getString("block", null);
        if (block != null) {
            return Optional.of(baseBuilder.apply(Component.blockNBT().pos(BlockNBTComponent.Pos.fromString(block))));
        }
        String storage = map.getString("storage", null);
        if (storage != null) {
            return Optional.of(baseBuilder.apply(Component.storageNBT().storage(Key.key(storage))));
        }
        throw new IllegalArgumentException("Don't know how to parse NBT component: " + map);
    }

    @VisibleForTesting
    NbtMapBuilder serializeNbtContentsComponent(NBTComponent<?> component) {
        NbtMapBuilder builder = NbtMap.builder();

        builder.putString("nbt", component.nbtPath());
        NbtUtil.putIfTrue(builder, "interpret", component.interpret());
        NbtUtil.putIfTrue(builder, "plain", component.plain());
        NbtUtil.checkNonNull(component.separator(), separator -> builder.put("separator", serialize(separator)));

        // switch case will intentionally break compile with new component types
        return switch (component) {
            case EntityNBTComponent entity -> builder.putString("entity", entity.selector());
            case BlockNBTComponent block -> builder.putString("block", block.pos().asString());
            case StorageNBTComponent storage -> builder.putString("storage", storage.storage().asString());
        };
    }

    @VisibleForTesting
    Optional<ComponentBuilder<?, ?>> deserializeFuzzyObjectComponent(NbtMap map) {
        Component fallback = deserializeOrNull(map.get("fallback"));
        String sprite = map.getString("sprite", null);
        if (sprite != null) {
            String atlas = map.getString("atlas", null);
            return Optional.of(Component.object()
                .contents(ObjectContents.sprite(atlas == null ? SpriteObjectContents.DEFAULT_ATLAS : Key.key(atlas), Key.key(sprite)))
                .fallback(fallback));
        }
        Object player = map.get("player");
        if (player != null) {
            PlayerHeadObjectContents.Builder contents = ResolvableProfileSerializerImpl.deserialize(player)
                .hat(map.getBoolean("hat", true));
            return Optional.of(Component.object()
                .contents(contents.build())
                .fallback(fallback));
        }
        return Optional.empty();
    }

    @VisibleForTesting
    NbtMapBuilder serializeObjectComponent(ObjectComponent component) {
        NbtMapBuilder builder = NbtMap.builder();
        NbtUtil.checkNonNull(component.fallback(), fallback -> builder.put("fallback", serialize(fallback)));

        // switch case will intentionally break compile with new component types
        return switch (component.contents()) {
            case SpriteObjectContents sprite -> {
                if (!sprite.atlas().equals(SpriteObjectContents.DEFAULT_ATLAS)) {
                    builder.putString("atlas", sprite.atlas().asString());
                }
                builder.putString("sprite", sprite.sprite().asString());
                yield builder;
            }
            case PlayerHeadObjectContents playerHead -> {
                builder.putCompound("player", ResolvableProfileSerializerImpl.serialize(playerHead));
                NbtUtil.putIfFalse(builder, "hat", playerHead.hat());
                yield builder;
            }
        };
    }
}
