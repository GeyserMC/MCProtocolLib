package org.geysermc.mcprotocollib.network;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public final class Flag<T extends @Nullable Object> {
    private final String key;
    private final Class<T> type;

    public T cast(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        return type.cast(obj);
    }

    public String key() {
        return key;
    }
}
