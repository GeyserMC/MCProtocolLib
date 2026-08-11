package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.cloudburstmc.nbt.NbtMap;

/**
 * Converts between an adventure {@link Component} and the nbt representation Minecraft: Java Edition
 * sends over the network, as described by vanilla's {@code ComponentSerialization} codec.
 *
 * <p>Note: {@code Object} is used rather than something narrower (such as {@link NbtMap}) since NBT
 * tags can be a plain string, list, or compound.
 *
 * <pre>{@code
 * Component component = NbtComponentSerializer.nbt().deserialize(tag);
 * Object tag = NbtComponentSerializer.nbt().serialize(component);
 * }</pre>
 *
 * @see #nbt() the shared instance, configured to match vanilla
 */
public interface NbtComponentSerializer extends ComponentSerializer<Component, Component, Object> {

    /**
     * Returns the shared serializer, configured to read and write tags the way vanilla does.
     */
    static NbtComponentSerializer nbt() {
        return NbtComponentSerializerImpl.INSTANCE;
    }

    /**
     * Returns a builder for a serializer that differs from {@link #nbt()}.
     */
    static Builder builder() {
        return new NbtComponentSerializerImpl.BuilderImpl();
    }

    /**
     * Reads a component from a tag.
     *
     * @param tag a string, a non-empty list of components, or a component compound
     * @throws NbtSerializationException if the tag is not a valid component
     */
    @Override
    Component deserialize(Object tag);

    /**
     * Writes a component as a tag.
     *
     * <p>A plain unstyled text component with no siblings collapses to a bare {@link String}, as it
     * does in vanilla; everything else becomes an {@link NbtMap}.
     *
     * @throws NbtSerializationException if the component cannot be represented as a tag
     */
    @Override
    Object serialize(Component component);

    /**
     * Reads the style fields of a component compound, ignoring any content fields alongside them.
     */
    Style deserializeStyle(NbtMap tag);

    /**
     * Writes a style as a compound of just its own fields. An empty style becomes an empty compound.
     */
    NbtMap serializeStyle(Style style);

    /**
     * Builds a serializer whose behaviour differs from vanilla's.
     */
    interface Builder {

        /**
         * Whether to write the discriminators that identify a component's content type and, for an
         * object component, its object variant. Defaults to {@code false}.
         *
         * <p>Vanilla only ever reads this field - its own encoder leaves it out and lets the
         * receiver work the content type out from which fields are present. Writing it makes the
         * intended type explicit, which costs a few bytes but lets a receiver skip the guessing and
         * report better errors. Either form is understood by a vanilla client.
         */
        Builder emitComponentType(boolean emitComponentType);

        NbtComponentSerializer build();
    }
}
