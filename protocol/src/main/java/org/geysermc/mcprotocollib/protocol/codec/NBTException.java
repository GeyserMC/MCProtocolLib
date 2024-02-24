package org.geysermc.mcprotocollib.protocol.codec;

import java.io.IOException;
import java.io.Serial;

public class NBTException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NBTException(IOException e) {
        super(e);
    }
}
