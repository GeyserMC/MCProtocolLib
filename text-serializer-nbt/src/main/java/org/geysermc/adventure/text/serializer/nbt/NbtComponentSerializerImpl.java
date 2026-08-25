package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.text.Component;

final class NbtComponentSerializerImpl implements NbtComponentSerializer {
    static final NbtComponentSerializerImpl INSTANCE = new NbtComponentSerializerImpl();

    @Override
    public Component deserialize(Object input) {
        return null;
    }

    @Override
    public Object serialize(Component component) {
        return null;
    }
}
