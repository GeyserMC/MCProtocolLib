package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.List;
import java.util.UUID;

final class HoverEventSerializerImpl {
    @VisibleForTesting
    static final List<HoverEvent.Action<?>> SUPPORTED_EVENTS = List.of(HoverEvent.Action.SHOW_TEXT, HoverEvent.Action.SHOW_ITEM,
        HoverEvent.Action.SHOW_ENTITY, HoverEvent.Action.SHOW_ACHIEVEMENT); // We throw on show_achievement, but that's intended

    static HoverEvent<?> deserialize(NbtMap map, NbtComponentSerializer componentSerializer) {
        HoverEvent.Action<?> action = HoverEvent.Action.NAMES.valueOrThrow(map.getString("action"));

        if (action == HoverEvent.Action.SHOW_TEXT) {
            return HoverEvent.showText(componentSerializer.deserialize(map.get("value")));
        } else if (action == HoverEvent.Action.SHOW_ITEM) {
            Key id = Key.key(map.getString("id"));
            int count = map.getInt("count", 1);
            NbtMap components = map.getCompound("components", null);
            // FIXME
            return HoverEvent.showItem(id, count, components == null ? null : BinaryTagHolder.binaryTagHolder(components.toString()));
        } else if (action == HoverEvent.Action.SHOW_ENTITY) {
            Key id = Key.key(map.getString("id"));
            UUID uuid = NbtUtil.deserializeLenientUUID(map.get("uuid"));
            Object name = map.get("name");
            return HoverEvent.showEntity(id, uuid, componentSerializer.deserializeOrNull(name));
        } else {
            throw new IllegalStateException("Don't know how to parse hover event action: " + action.name());
        }
    }

    static NbtMap serialize(HoverEvent<?> event, NbtComponentSerializer componentSerializer) {
        NbtMapBuilder builder = NbtMap.builder();
        builder.putString("action", event.action().name());

        HoverEvent.Action<?> action = event.action();
        Object value = event.value();

        if (action == HoverEvent.Action.SHOW_TEXT) {
            builder.put("value", componentSerializer.serialize((Component) value));
        } else if (action == HoverEvent.Action.SHOW_ITEM) {
            HoverEvent.ShowItem item = (HoverEvent.ShowItem) value;
            builder.put("id", item.item().asString());
            if (item.count() != 1) {
                builder.put("count", item.count());
            }
            // FIXME data components
        } else if (action == HoverEvent.Action.SHOW_ENTITY) {
            HoverEvent.ShowEntity entity = (HoverEvent.ShowEntity) value;
            builder.putString("id", entity.type().asString());
            builder.putIntArray("uuid", NbtUtil.serializeUUID(entity.id()));

            if (entity.name() != null) {
                builder.put("name", componentSerializer.serialize(entity.name()));
            }
        } else {
            throw new IllegalStateException("Don't know how to encode hover event action: " + action.name());
        }

        return builder.build();
    }
}
