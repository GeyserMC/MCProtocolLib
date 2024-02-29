package org.geysermc.mcprotocollib.network;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Flag<T> {
    private final String key;
    private final Class<T> type;

    public T cast(Object obj) {
        return type.cast(obj);
    }

    public String key() {
        return key;
    }
}
