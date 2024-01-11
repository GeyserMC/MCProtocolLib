package com.github.steveice10.mc.protocol.data.game;

import java.util.function.Consumer;

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

    Holder<T> ifId(Consumer<Holder<T>> action);

    Holder<T> ifCustom(Consumer<Holder<T>> action);

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
        public Holder<T> ifId(Consumer<Holder<T>> action) {
            action.accept(this);
            return this;
        }

        @Override
        public Holder<T> ifCustom(Consumer<Holder<T>> action) {
            // no-op
            return this;
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
        public Holder<T> ifId(Consumer<Holder<T>> action) {
            return this;
        }

        @Override
        public Holder<T> ifCustom(Consumer<Holder<T>> action) {
            action.accept(this);
            return this;
        }
    }
}
