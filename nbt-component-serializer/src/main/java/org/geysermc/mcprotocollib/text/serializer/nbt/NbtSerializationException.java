package org.geysermc.mcprotocollib.text.serializer.nbt;

import java.io.Serial;

/**
 * Thrown when a tag cannot be read as a component, or a component cannot be written as a tag.
 */
public class NbtSerializationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NbtSerializationException(final String message) {
        super(message);
    }

    public NbtSerializationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
