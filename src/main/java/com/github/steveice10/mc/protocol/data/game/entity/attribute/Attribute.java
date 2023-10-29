package com.github.steveice10.mc.protocol.data.game.entity.attribute;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record Attribute(@NotNull AttributeType type, double value, @NotNull List<AttributeModifier> modifiers) {
    public Attribute(@NotNull AttributeType type) {
        this(type, 0);
    }

    public Attribute(@NotNull AttributeType type, double value) {
        this(type, value, new ArrayList<>());
    }
}
