package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public interface NbtComponentSerializer extends ComponentSerializer<Component, Component, Object> {

    static NbtComponentSerializer nbt() {
        return NbtComponentSerializerImpl.INSTANCE;
    }

    interface Builder extends AbstractBuilder<NbtComponentSerializer> {
    }
}
