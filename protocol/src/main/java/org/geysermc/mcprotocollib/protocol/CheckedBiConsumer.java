package org.geysermc.mcprotocollib.protocol;

@FunctionalInterface
public interface CheckedBiConsumer<T, U, E extends Throwable> {
    void accept(T t, U u) throws E;
}
