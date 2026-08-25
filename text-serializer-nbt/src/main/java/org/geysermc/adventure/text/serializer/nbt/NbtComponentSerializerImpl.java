package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;

import java.util.List;

final class NbtComponentSerializerImpl implements NbtComponentSerializer {
    static final NbtComponentSerializerImpl INSTANCE = new NbtComponentSerializerImpl();

    @Override
    public Component deserialize(Object input) {
        if (input instanceof String simpleText) {
            return Component.text(simpleText);
        } else if (input instanceof List<?> list) {
            return Component.join(JoinConfiguration.noSeparators(), list.stream().map(this::deserialize).toList());
        }
        return null;
    }

    @Override
    public Object serialize(Component component) {
        if (component instanceof TextComponent text && component.children().isEmpty() && !component.hasStyling()) {
            return text.content();
        }

        NbtMapBuilder builder = NbtMap.builder();

        if (!component.children().isEmpty()) {
            builder.put("extra", component.children().stream().map(this::serialize).collect(HeterogeneousNbtList.collector()).build());
        }
        StyleSerializerImpl.serialize(builder, component.style(), this);

        return builder.build();
    }
}
