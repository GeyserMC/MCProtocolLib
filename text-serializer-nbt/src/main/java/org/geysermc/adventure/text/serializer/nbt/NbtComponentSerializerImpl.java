package org.geysermc.adventure.text.serializer.nbt;

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
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;

import java.util.List;

final class NbtComponentSerializerImpl implements NbtComponentSerializer {
    static final NbtComponentSerializerImpl INSTANCE = new NbtComponentSerializerImpl();

    @Override
    public Component deserialize(Object input) {
        return switch (input) {
            case String simpleText -> Component.text(simpleText);
            case NbtList<?> list -> Component.join(JoinConfiguration.noSeparators(), HeterogeneousNbtList.tryUnwrap(list).stream().map(this::deserialize).toList());
            case NbtMap map -> {
                Style style = StyleSerializerImpl.deserialize(map, this);
                Object extra = map.get("extra");
                List<Component> children;
                if (extra instanceof NbtList<?> list) {
                    children = HeterogeneousNbtList.tryUnwrap(list).stream().map(this::deserialize).toList();
                }
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

    private Object serializeTranslationArgument(TranslationArgument argument) {
        if (argument.value() instanceof Component component) {
            return serialize(component);
        }
        return argument.value();
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
