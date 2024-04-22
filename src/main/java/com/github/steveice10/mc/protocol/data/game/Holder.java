package com.github.steveice10.mc.protocol.data.game;

import lombok.Data;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Data
public class Holder<T> {
    private final int id;
    private final T customValue;
    private final boolean custom;

    public Holder(int id) {
        this.id = id;
        this.customValue = null;
        this.custom = false;
    }

    public Holder(T customValue) {
        this.id = -1;
        this.customValue = customValue;
        this.custom = true;
    }

    public Holder<T> ifId(Consumer<Holder<T>> action) {
        if (!custom) {
            action.accept(this);
        }
        return this;
    }

    public Holder<T> ifCustom(Consumer<Holder<T>> action) {
        if (custom) {
            action.accept(this);
        }
        return this;
    }
}
