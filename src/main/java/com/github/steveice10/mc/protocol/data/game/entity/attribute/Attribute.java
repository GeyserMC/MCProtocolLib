package com.github.steveice10.mc.protocol.data.game.entity.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Attribute {
    private final @NonNull AttributeType type;
    private final double value;
    private final @NonNull List<AttributeModifier> modifiers;

    public Attribute(@NonNull AttributeType type) {
        this(type, 0);
    }

    public Attribute(@NonNull AttributeType type, double value) {
        this(type, value, new ArrayList<>());
    }
}
