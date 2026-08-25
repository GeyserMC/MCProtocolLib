package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.Style;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;

import java.util.List;

final class NbtComponentSerializerImpl implements NbtComponentSerializer {
    static final NbtComponentSerializerImpl INSTANCE = new NbtComponentSerializerImpl();

    @Override
    public Component deserialize(Object input) {
        if (input instanceof String simpleText) {
            return Component.text(simpleText);
        } else if (input instanceof NbtList<?> list) {
            return Component.join(JoinConfiguration.noSeparators(), HeterogeneousNbtList.tryUnwrap(list).stream().map(this::deserialize).toList());
        } else if (input instanceof NbtMap map) {
            Style style = StyleSerializerImpl.deserialize(map, this);
            Object extra = map.get("extra");
            List<Component> children;
            if (extra instanceof NbtList<?> list) {
                children = HeterogeneousNbtList.tryUnwrap(list).stream().map(this::deserialize).toList();
            }
        }
        throw new IllegalArgumentException("Don't know how to parse component: " + input);
    }

    @Override
    public Object serialize(Component component) {
        if (component instanceof TextComponent text && component.children().isEmpty() && !component.hasStyling()) {
            return text.content();
        }

        NbtMapBuilder builder = NbtMap.builder();

        switch (component) {
            case TextComponent text -> {}
            case TranslatableComponent translatable -> {}
            case KeybindComponent keybind -> {}
            case ScoreComponent score -> {}
            case SelectorComponent selector -> {}
            case NBTComponent<?> nbtComponent -> {}
            case ObjectComponent objectComponent -> {}
        }

        if (!component.children().isEmpty()) {
            builder.put("extra", component.children().stream().map(this::serialize).collect(HeterogeneousNbtList.collector()).build());
        }
        StyleSerializerImpl.serialize(builder, component.style(), this);

        return builder.build();
    }
}
