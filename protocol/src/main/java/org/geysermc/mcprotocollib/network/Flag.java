package org.geysermc.mcprotocollib.network;

public record Flag<T>(String key, Class<T> type) {
    public T cast(Object obj) {
        return type.cast(obj);
    }
}
