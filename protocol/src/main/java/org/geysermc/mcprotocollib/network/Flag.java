package org.geysermc.mcprotocollib.network;

public record Flag<T>(String key, Class<T> clazz) {
    public T cast(Object obj) {
        return clazz.cast(obj);
    }
}
