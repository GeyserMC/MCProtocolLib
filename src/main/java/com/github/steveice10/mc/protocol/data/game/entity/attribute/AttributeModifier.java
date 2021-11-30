package com.github.steveice10.mc.protocol.data.game.entity.attribute;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class AttributeModifier {
    /**
     * Use {@link ModifierType} to determine built-in modifiers.
     */
    private final @NonNull UUID uuid;
    private final double amount;
    private final @NonNull ModifierOperation operation;

    public AttributeModifier(@NonNull UUID uuid, double amount, @NonNull ModifierOperation operation) {
        this.uuid = uuid;
        this.amount = amount;
        this.operation = operation;
    }
}
