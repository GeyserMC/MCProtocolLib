package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickEvent;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;

import java.util.Optional;

final class ClickEventSerializerImpl {

    static ClickEvent<?> deserialize(NbtMap map) {
        ClickEvent.Action<?> action = ClickEvent.Action.NAMES.valueOrThrow(map.getString("action"));

        return switch (action) {
            case ClickEvent.Action.OpenUrl ignored -> ClickEvent.openUrl(map.getString("url"));
            case ClickEvent.Action.OpenFile ignored -> ClickEvent.openFile(map.getString("path"));
            case ClickEvent.Action.RunCommand ignored -> ClickEvent.runCommand(map.getString("command"));
            case ClickEvent.Action.SuggestCommand ignored -> ClickEvent.suggestCommand(map.getString("command"));
            case ClickEvent.Action.ChangePage ignored -> ClickEvent.changePage(map.getInt("page"));
            case ClickEvent.Action.CopyToClipboard ignored -> ClickEvent.copyToClipboard(map.getString("value"));
            case ClickEvent.Action.ShowDialog ignored -> {
                Object tag = map.get("dialog");
                if (tag instanceof String reference) {
                    yield ClickEvent.showDialog(new NbtDialog(Key.key(reference)));
                } else if (tag instanceof NbtMap inline) {
                    yield ClickEvent.showDialog(new NbtDialog(inline));
                }
                throw new IllegalStateException("Expected \"dialog\" of \"show_dialog\" click event to be a string reference or NbtMap, got: " + tag.getClass());
            }
            case ClickEvent.Action.Custom ignored -> {
                Key id = Key.key(map.getString("id"));
                NbtMap payload = map.getCompound("payload");
                if (payload.isEmpty()) {
                    yield ClickEvent.custom(id);
                }
                // FIXME
                yield ClickEvent.custom(id, BinaryTagHolder.binaryTagHolder(payload.toString()));
            }
        };
    }

    static NbtMap serialize(ClickEvent<?> event) {
        NbtMapBuilder builder = NbtMap.builder();
        builder.putString("action", event.action().name());
        ClickEvent.Payload payload = event.payload();

        // Doing it like this to cause compilation error when new actions are added
        switch (event.action()) {
            case ClickEvent.Action.OpenUrl ignored -> builder.putString("url", asText(payload));
            case ClickEvent.Action.OpenFile ignored -> builder.putString("path", asText(payload));
            case ClickEvent.Action.RunCommand ignored -> builder.putString("command", asText(payload));
            case ClickEvent.Action.SuggestCommand ignored -> builder.putString("command", asText(payload));
            case ClickEvent.Action.ChangePage ignored -> builder.putInt("page", asInt(payload));
            case ClickEvent.Action.CopyToClipboard ignored -> builder.putString("value", asText(payload));
            case ClickEvent.Action.ShowDialog ignored -> {
                ClickEvent.Payload.Dialog dialogPayload = (ClickEvent.Payload.Dialog) payload;
                if (dialogPayload.dialog() instanceof NbtDialog(Optional<Key> reference, Optional<NbtMap> inline)) {
                    reference.ifPresent(key -> builder.putString("dialog", key.asString()));
                    inline.ifPresent(map -> builder.putCompound("dialog", map));
                } else {
                    // FIXME
                }
            }
            case ClickEvent.Action.Custom ignored -> {
                ClickEvent.Payload.Custom customPayload = (ClickEvent.Payload.Custom) payload;
                builder.putString("id", customPayload.key().asString());

                if (customPayload.nbt() != null) {
                    // FIXME
                }
            }
        }

        return builder.build();
    }

    private static String asText(ClickEvent.Payload payload) {
        return ((ClickEvent.Payload.Text) payload).value();
    }

    private static int asInt(ClickEvent.Payload payload) {
        return ((ClickEvent.Payload.Int) payload).integer();
    }
}
