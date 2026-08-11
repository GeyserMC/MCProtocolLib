package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;

/**
 * Reads and writes a single entry of a translatable component's {@code with} list.
 *
 * <p>Vanilla stores an entry as an {@code Either<Primitive, Component>} and tries the primitive
 * branch first, so any number tag or string tag is taken as a literal argument and only a compound
 * or a list is parsed as a nested component.
 *
 * <p>Two places where adventure's model is narrower than the wire format, both harmless:
 *
 * <ul>
 *   <li>Nbt has no boolean type, so {@link TranslationArgument#bool(boolean)} is written as a byte
 *       and comes back as {@link TranslationArgument#numeric(Number)} holding {@code (byte) 1}. That
 *       is what vanilla does with it too - {@code ExtraCodecs.JAVA} keeps the primitive a number and
 *       never restores a boolean - so the wire data round trips exactly even though the adventure
 *       value does not.
 *   <li>Adventure has no string argument, so a string tag becomes a component argument wrapping a
 *       literal. That round trips back to a bare string, because the core serializer collapses an
 *       unstyled childless literal to a plain string.
 * </ul>
 */
final class TranslationArgumentSerializer {

    private final NbtComponentSerializerImpl serializer;

    TranslationArgumentSerializer(final NbtComponentSerializerImpl serializer) {
        this.serializer = serializer;
    }

    TranslationArgument deserialize(final Object tag) {
        if (tag instanceof Number number) {
            return TranslationArgument.numeric(number);
        } else if (tag instanceof String text) {
            return TranslationArgument.component(Component.text(text));
        } else if (tag instanceof NbtMap || tag instanceof NbtList<?>) {
            return TranslationArgument.component(this.serializer.deserialize(tag));
        }
        throw new NbtSerializationException("A translation argument must be a number, a string or a component, got "
            + Tags.typeNameOf(tag));
    }

    Object serialize(final TranslationArgument argument) {
        final Object value = argument.value();
        return switch (value) {
            case Boolean bool -> Tags.writeBoolean(bool);
            case Number number ->
                // Handed back as-is so the argument keeps the tag type it was created with
                number;
            case Component component ->
                // Yields a bare string for a collapsible literal, matching vanilla's primitive branch
                this.serializer.serialize(component);
            default -> throw new NbtSerializationException("Unknown translation argument value type "
                + Tags.typeNameOf(value));
        };
    }
}
