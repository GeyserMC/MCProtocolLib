package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.cloudburstmc.nbt.NbtMap;

/**
 * This serializer serializes and deserializes {@link Component}s to and from NBT tags represented using <a href="https://github.com/cloudburstMC/nbt">Cloudburst's NBT library</a>.
 *
 * <p>The serializer supports all modern component, click, and hover event types. It does not support serializing and deserializing older component formats - only the
 * latest Minecraft version is supported. Implementation notes are as follows:</p>
 *
 * <ul>
 *     <li>The serializer implements {@link DialogLike} to deserialize {@code "show_dialog"} click events. The implementation can be found at {@link NbtDialog}. It is a simple implementation,
 *     only holding the {@link Key} reference to a dialog, or the inline-defined the dialog, serialized as {@link NbtMap}. The serializer can only ever serialize {@code "show_dialog"}
 *     click events with a {@link NbtDialog} {@link DialogLike} implementation, it will throw {@link IllegalArgumentException} for other implementations.</li>
 *     <li>The serializer has its own implementation of {@link BinaryTagHolder} to deserialize data components for the {@code "show_item"} hover event, and NBT payloads for the {@code "custom"} click event.
 *     Please note that this implementation does not follow the official specification; see the notes there for details. Generally, the serializer is likely to fail and throw a {@link RuntimeException}
 *     when trying to serialize other {@link BinaryTagHolder} implementations.</li>
 * </ul>
 *
 * @see NbtDialog
 * @see NbtBinaryTagHolder
 */
public interface NbtComponentSerializer extends ComponentSerializer<Component, Component, Object> {

    static NbtComponentSerializer nbt() {
        return NbtComponentSerializerImpl.INSTANCE;
    }
}
