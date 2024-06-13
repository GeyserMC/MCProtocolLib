package org.geysermc.mcprotocollib.protocol.data.game;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

/**
 * Represents an object that could either be a network ID, or a custom-defined one.
 */
public interface Holder<T> {
    static <T> Holder<T> ofId(int id) {
        return new IdHolder<>(id);
    }

    static <T> Holder<T> ofCustom(T object) {
        return new CustomHolder<>(object);
    }

    boolean isId();

    int id();

    boolean isCustom();

    T custom();

    Holder<T> ifId(IntConsumer action);

    Holder<T> ifCustom(Consumer<T> action);

    /**
     * Returns the holder as an object, or else looks up the item in the registry.
     */
    T getOrCompute(IntFunction<T> supplier);

    record IdHolder<T>(int id) implements Holder<T> {
        @Override
        public boolean isId() {
            return true;
        }

        @Override
        public boolean isCustom() {
            return false;
        }

        @Override
        public T custom() {
            throw new IllegalStateException("Check isCustom first!");
        }

        @Override
        public Holder<T> ifId(IntConsumer action) {
            action.accept(id);
            return this;
        }

        @Override
        public Holder<T> ifCustom(Consumer<T> action) {
            // no-op
            return this;
        }

        @Override
        public T getOrCompute(IntFunction<T> supplier) {
            return supplier.apply(id);
        }
    }

    record CustomHolder<T>(T object) implements Holder<T> {
        @Override
        public boolean isId() {
            return false;
        }

        @Override
        public int id() {
            throw new IllegalStateException("Check isId first!");
        }

        @Override
        public boolean isCustom() {
            return true;
        }

        @Override
        public T custom() {
            return object;
        }

        @Override
        public Holder<T> ifId(IntConsumer action) {
            return this;
        }

        @Override
        public Holder<T> ifCustom(Consumer<T> action) {
            action.accept(object);
            return this;
        }

        @Override
        public T getOrCompute(IntFunction<T> supplier) {
            return object;
        }
    }
}
