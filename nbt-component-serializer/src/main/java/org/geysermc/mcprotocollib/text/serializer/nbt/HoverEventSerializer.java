package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Reads and writes the hover event of a style.
 *
 * <p>Vanilla's {@code HoverEvent} is a sealed interface whose codecs are map codecs, so an action's
 * fields sit next to the {@code action} field rather than under a wrapper - there is no
 * {@code contents} or {@code value} object around them any more, that was the pre-1.21.5 shape.
 *
 * <p>{@code show_achievement} is an adventure-only leftover from 1.11 and has no nbt form at all, so
 * it is rejected rather than guessed at.
 */
final class HoverEventSerializer {

    /**
     * Vanilla's {@code ItemStack} count is {@code ExtraCodecs.intRange(1, 99)}, defaulting to one, and
     * a default-valued field is left out of the output entirely.
     */
    private static final int DEFAULT_ITEM_COUNT = 1;
    private static final int MAX_ITEM_COUNT = 99;

    /**
     * Vanilla's {@code DataComponentPatch} writes a removal as the component's id prefixed with this,
     * mapped to an empty value, because nbt has no way to write "this key is explicitly absent".
     */
    private static final String REMOVED_COMPONENT_PREFIX = "!";

    private final NbtComponentSerializerImpl serializer;

    HoverEventSerializer(final NbtComponentSerializerImpl serializer) {
        this.serializer = serializer;
    }

    // Reading

    HoverEvent<?> deserialize(final NbtMap tag) {
        final String name = Tags.getRequiredString(tag, ComponentFields.ACTION);
        final HoverEvent.Action<?> action = HoverEvent.Action.NAMES.value(name);
        if (action == null) {
            throw new NbtSerializationException("Unknown hover event action '" + name + "'");
        }

        if (action == HoverEvent.Action.SHOW_TEXT) {
            return HoverEvent.showText(this.deserializeRequiredComponent(tag, ComponentFields.HOVER_SHOW_TEXT_VALUE));
        } else if (action == HoverEvent.Action.SHOW_ITEM) {
            return this.deserializeItem(tag);
        } else if (action == HoverEvent.Action.SHOW_ENTITY) {
            return this.deserializeEntity(tag);
        }

        throw new NbtSerializationException("A " + name + " hover event has no nbt form");
    }

    private HoverEvent<HoverEvent.ShowItem> deserializeItem(final NbtMap tag) {
        final Key item = Key.key(Tags.getRequiredString(tag, ComponentFields.HOVER_ITEM_ID));

        final Number rawCount = Tags.getNumber(tag, ComponentFields.HOVER_ITEM_COUNT);
        final int count = rawCount == null ? DEFAULT_ITEM_COUNT : rawCount.intValue();
        if (count < DEFAULT_ITEM_COUNT || count > MAX_ITEM_COUNT) {
            throw new NbtSerializationException("A show_item hover event needs a count between "
                + DEFAULT_ITEM_COUNT + " and " + MAX_ITEM_COUNT + ", got " + count);
        }

        return HoverEvent.showItem(item, count, deserializeDataComponents(tag));
    }

    /**
     * Each value is kept as the tag it was read from: a data component is opaque to a text component
     * serializer, and turning it into a string here would cost a parse on the way back for no gain.
     */
    private static Map<Key, DataComponentValue> deserializeDataComponents(final NbtMap tag) {
        final NbtMap components = Tags.getCompound(tag, ComponentFields.HOVER_ITEM_COMPONENTS);
        if (components == null || components.isEmpty()) {
            return Map.of();
        }

        final Map<Key, DataComponentValue> values = new LinkedHashMap<>(components.size());
        for (final Map.Entry<String, Object> entry : components.entrySet()) {
            final String id = entry.getKey();
            if (id.startsWith(REMOVED_COMPONENT_PREFIX)) {
                values.put(Key.key(id.substring(REMOVED_COMPONENT_PREFIX.length())), DataComponentValue.removed());
            } else {
                values.put(Key.key(id), NbtDataComponentValue.nbtDataComponentValue(entry.getValue()));
            }
        }
        return values;
    }

    private HoverEvent<HoverEvent.ShowEntity> deserializeEntity(final NbtMap tag) {
        final Key type = Key.key(Tags.getRequiredString(tag, ComponentFields.HOVER_ENTITY_ID));
        final UUID id = deserializeUuid(tag.get(ComponentFields.HOVER_ENTITY_UUID));
        final Component name = this.serializer.deserializeOptionalComponent(tag, ComponentFields.HOVER_ENTITY_NAME);

        return name == null ? HoverEvent.showEntity(type, id) : HoverEvent.showEntity(type, id, name);
    }

    /**
     * Vanilla's {@code UUIDUtil.LENIENT_CODEC} accepts both the four int form and the dashed string
     * form, and this follows it so that hand written and older data still read.
     */
    private static UUID deserializeUuid(final @Nullable Object tag) {
        if (tag instanceof int[] bits) {
            if (bits.length != 4) {
                throw new NbtSerializationException("A uuid written as ints needs exactly 4 of them, got " + bits.length);
            }
            return new UUID(
                (long) bits[0] << 32 | bits[1] & 0xFFFFFFFFL,
                (long) bits[2] << 32 | bits[3] & 0xFFFFFFFFL);
        } else if (tag instanceof String text) {
            try {
                return UUID.fromString(text);
            } catch (final IllegalArgumentException exception) {
                throw new NbtSerializationException("Not a valid uuid: '" + text + "'", exception);
            }
        }
        throw new NbtSerializationException("Expected field '" + ComponentFields.HOVER_ENTITY_UUID
            + "' to be an int array or a string, got " + Tags.typeNameOf(tag));
    }

    private Component deserializeRequiredComponent(final NbtMap tag, final String key) {
        final Component component = this.serializer.deserializeOptionalComponent(tag, key);
        if (component == null) {
            throw new NbtSerializationException("Missing required component field '" + key + "'");
        }
        return component;
    }

    // Writing

    NbtMap serialize(final HoverEvent<?> event) {
        final HoverEvent.Action<?> action = event.action();
        final NbtMapBuilder tag = NbtMap.builder().putString(ComponentFields.ACTION, action.name());

        if (action == HoverEvent.Action.SHOW_TEXT) {
            tag.put(ComponentFields.HOVER_SHOW_TEXT_VALUE, this.serializer.serialize((Component) event.value()));
        } else if (action == HoverEvent.Action.SHOW_ITEM) {
            serializeItem((HoverEvent.ShowItem) event.value(), tag);
        } else if (action == HoverEvent.Action.SHOW_ENTITY) {
            this.serializeEntity((HoverEvent.ShowEntity) event.value(), tag);
        } else {
            throw new NbtSerializationException("A " + action.name() + " hover event has no nbt form");
        }

        return tag.build();
    }

    private static void serializeItem(final HoverEvent.ShowItem item, final NbtMapBuilder tag) {
        tag.putString(ComponentFields.HOVER_ITEM_ID, item.item().asString());

        final int count = item.count();
        if (count != DEFAULT_ITEM_COUNT) {
            tag.putInt(ComponentFields.HOVER_ITEM_COUNT, count);
        }

        final Map<Key, DataComponentValue> components = item.dataComponents();
        if (!components.isEmpty()) {
            final NbtMapBuilder serialized = NbtMap.builder();
            for (final Map.Entry<Key, DataComponentValue> component : components.entrySet()) {
                serializeDataComponent(component.getKey(), component.getValue(), serialized);
            }
            tag.putCompound(ComponentFields.HOVER_ITEM_COMPONENTS, serialized.build());
        }
    }

    private static void serializeDataComponent(final Key id, final DataComponentValue value, final NbtMapBuilder tag) {
        switch (value) {
            case NbtDataComponentValue nbt -> tag.put(id.asString(), nbt.tag());
            case DataComponentValue.Removed ignored -> tag.putCompound(REMOVED_COMPONENT_PREFIX + id.asString(), NbtMap.EMPTY);
            case DataComponentValue.TagSerializable serializable ->
                // Written against some other nbt model, so the string form is the only shape both sides know
                tag.put(id.asString(), SnbtCodec.fromSnbt(serializable.asBinaryTag().string()));
            default -> throw new NbtSerializationException("Cannot write data component '" + id.asString()
                + "' of type " + value.getClass().getName() + " as nbt");
        }
    }

    private void serializeEntity(final HoverEvent.ShowEntity entity, final NbtMapBuilder tag) {
        tag.putString(ComponentFields.HOVER_ENTITY_ID, entity.type().asString());
        tag.putIntArray(ComponentFields.HOVER_ENTITY_UUID, serializeUuid(entity.id()));

        final Component name = entity.name();
        if (name != null) {
            tag.put(ComponentFields.HOVER_ENTITY_NAME, this.serializer.serialize(name));
        }
    }

    /**
     * Always the four int form, which is what vanilla's {@code UUIDUtil.CODEC} writes.
     */
    private static int[] serializeUuid(final UUID uuid) {
        final long most = uuid.getMostSignificantBits();
        final long least = uuid.getLeastSignificantBits();
        return new int[]{(int) (most >> 32), (int) most, (int) (least >> 32), (int) least};
    }
}
