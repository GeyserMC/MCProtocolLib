package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickEvent;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.jspecify.annotations.Nullable;

/**
 * Reads and writes the click event of a style.
 *
 * <p>Vanilla's {@code ClickEvent} is a sealed interface with one codec per action, so the fields that
 * carry the action's data are named after that action rather than shared: {@code open_url} carries a
 * {@code url}, {@code run_command} a {@code command}, and so on. There is no single {@code value}
 * field any more - that was the pre-1.21.5 shape.
 *
 * <p>Two actions do not survive a round trip in full:
 * <ul>
 *     <li>{@code show_dialog} carries either an inline dialog or a reference into the dialog registry.
 *     adventure models the payload as a {@link DialogLike}, which is an empty marker interface with no
 *     factory and nothing to read back, so an inline dialog cannot be represented and is rejected. A
 *     registry reference is kept as a {@link DialogReference}, which is also {@link Keyed} so that any
 *     consumer can recover the key; any other {@link Keyed} dialog is written back as a reference too.</li>
 *     <li>{@code open_file} is only ever produced by a client's own code - vanilla's
 *     {@code Action#filterForSerialization} drops it on the wire - so a vanilla server or client will
 *     reject it. It is still handled in both directions, because it is valid adventure data and this
 *     serializer is also used off the wire.</li>
 * </ul>
 */
final class ClickEventSerializer {

    private ClickEventSerializer() {
    }

    // Reading

    static ClickEvent<?> deserialize(final NbtMap tag) {
        final String name = Tags.getRequiredString(tag, ComponentFields.ACTION);
        final ClickEvent.Action<?> action = ClickEvent.Action.NAMES.value(name);
        if (action == null) {
            throw new NbtSerializationException("Unknown click event action '" + name + "'");
        }

        if (action == ClickEvent.Action.OPEN_URL) {
            return ClickEvent.openUrl(Tags.getRequiredString(tag, ComponentFields.CLICK_URL));
        } else if (action == ClickEvent.Action.OPEN_FILE) {
            return ClickEvent.openFile(Tags.getRequiredString(tag, ComponentFields.CLICK_PATH));
        } else if (action == ClickEvent.Action.RUN_COMMAND) {
            return ClickEvent.runCommand(Tags.getRequiredString(tag, ComponentFields.CLICK_COMMAND));
        } else if (action == ClickEvent.Action.SUGGEST_COMMAND) {
            return ClickEvent.suggestCommand(Tags.getRequiredString(tag, ComponentFields.CLICK_COMMAND));
        } else if (action == ClickEvent.Action.CHANGE_PAGE) {
            return ClickEvent.changePage(deserializePage(tag));
        } else if (action == ClickEvent.Action.COPY_TO_CLIPBOARD) {
            return ClickEvent.copyToClipboard(Tags.getRequiredString(tag, ComponentFields.CLICK_VALUE));
        } else if (action == ClickEvent.Action.SHOW_DIALOG) {
            return ClickEvent.showDialog(deserializeDialog(tag));
        } else if (action == ClickEvent.Action.CUSTOM) {
            return deserializeCustom(tag);
        }

        throw new NbtSerializationException("Unsupported click event action '" + name + "'");
    }

    /**
     * Vanilla's page field is {@code ExtraCodecs.POSITIVE_INT}, so zero and negative pages are not just
     * meaningless, they are rejected before the event is built.
     */
    private static int deserializePage(final NbtMap tag) {
        final Number page = Tags.getNumber(tag, ComponentFields.CLICK_PAGE);
        if (page == null) {
            throw new NbtSerializationException("Missing required int field '" + ComponentFields.CLICK_PAGE + "'");
        }

        final int value = page.intValue();
        if (value < 1) {
            throw new NbtSerializationException("A change_page click event needs a page of at least 1, got " + value);
        }
        return value;
    }

    private static ClickEvent<?> deserializeCustom(final NbtMap tag) {
        final Key id = Key.key(Tags.getRequiredString(tag, ComponentFields.CLICK_ID));

        // The payload is whatever the sender put there. It is never inspected, only carried, so it is
        // handed to adventure as an opaque encoded tag
        final Object payload = tag.get(ComponentFields.CLICK_PAYLOAD);
        return payload == null
            ? ClickEvent.custom(id)
            : ClickEvent.custom(id, BinaryTagHolder.encode(payload, SnbtCodec.CODEC));
    }

    private static DialogLike deserializeDialog(final NbtMap tag) {
        final Object dialog = tag.get(ComponentFields.CLICK_DIALOG);
        if (dialog instanceof String reference) {
            return new DialogReference(Key.key(reference));
        } else if (dialog instanceof NbtMap) {
            throw new NbtSerializationException("An inline dialog of a show_dialog click event cannot be read: "
                + "adventure has no api to build one, only a registry reference is supported");
        }
        throw new NbtSerializationException("Expected field '" + ComponentFields.CLICK_DIALOG
            + "' to be a string or a compound, got " + Tags.typeNameOf(dialog));
    }

    // Writing

    static NbtMap serialize(final ClickEvent<?> event) {
        final ClickEvent.Action<?> action = event.action();
        final ClickEvent.Payload payload = event.payload();
        final NbtMapBuilder tag = NbtMap.builder().putString(ComponentFields.ACTION, action.name());

        if (action == ClickEvent.Action.OPEN_URL) {
            tag.putString(ComponentFields.CLICK_URL, text(action, payload));
        } else if (action == ClickEvent.Action.OPEN_FILE) {
            tag.putString(ComponentFields.CLICK_PATH, text(action, payload));
        } else if (action == ClickEvent.Action.RUN_COMMAND || action == ClickEvent.Action.SUGGEST_COMMAND) {
            tag.putString(ComponentFields.CLICK_COMMAND, text(action, payload));
        } else if (action == ClickEvent.Action.CHANGE_PAGE) {
            tag.putInt(ComponentFields.CLICK_PAGE, integer(action, payload));
        } else if (action == ClickEvent.Action.COPY_TO_CLIPBOARD) {
            tag.putString(ComponentFields.CLICK_VALUE, text(action, payload));
        } else if (action == ClickEvent.Action.SHOW_DIALOG) {
            tag.putString(ComponentFields.CLICK_DIALOG, serializeDialog(action, payload));
        } else if (action == ClickEvent.Action.CUSTOM) {
            serializeCustom(action, payload, tag);
        } else {
            throw new NbtSerializationException("Unsupported click event action '" + action.name() + "'");
        }

        return tag.build();
    }

    private static void serializeCustom(final ClickEvent.Action<?> action, final ClickEvent.Payload payload,
                                        final NbtMapBuilder tag) {
        if (!(payload instanceof ClickEvent.Payload.Custom custom)) {
            throw payloadMismatch(action, payload, "a custom payload");
        }

        tag.putString(ComponentFields.CLICK_ID, custom.key().asString());

        final BinaryTagHolder nbt = custom.nbt();
        if (nbt != null) {
            tag.put(ComponentFields.CLICK_PAYLOAD, nbt.get(SnbtCodec.CODEC));
        }
    }

    private static String serializeDialog(final ClickEvent.Action<?> action, final ClickEvent.Payload payload) {
        if (!(payload instanceof ClickEvent.Payload.Dialog dialogPayload)) {
            throw payloadMismatch(action, payload, "a dialog payload");
        }

        final DialogLike dialog = dialogPayload.dialog();
        if (dialog instanceof Keyed keyed) {
            return keyed.key().asString();
        }
        throw new NbtSerializationException("A show_dialog click event can only be written as a registry reference, "
            + "and this dialog carries no key: " + dialog.getClass().getName());
    }

    private static String text(final ClickEvent.Action<?> action, final ClickEvent.Payload payload) {
        if (payload instanceof ClickEvent.Payload.Text value) {
            return value.value();
        }
        throw payloadMismatch(action, payload, "a text payload");
    }

    private static int integer(final ClickEvent.Action<?> action, final ClickEvent.Payload payload) {
        if (payload instanceof ClickEvent.Payload.Int value) {
            return value.integer();
        }
        throw payloadMismatch(action, payload, "an int payload");
    }

    private static NbtSerializationException payloadMismatch(final ClickEvent.Action<?> action,
                                                            final ClickEvent.@Nullable Payload payload,
                                                            final String expected) {
        return new NbtSerializationException("A " + action.name() + " click event needs " + expected + ", got "
            + (payload == null ? "nothing" : payload.getClass().getName()));
    }

    /**
     * A {@code show_dialog} payload that points at an entry of the dialog registry.
     *
     * <p>{@link DialogLike} carries no state of its own, so this is the whole of what adventure lets a
     * reference be. It is {@link Keyed} so that the key survives for anything that reads the event back,
     * including {@link #serialize(ClickEvent)}.
     */
    record DialogReference(Key key) implements DialogLike, Keyed {
    }
}
