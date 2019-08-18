package com.github.steveice10.mc.protocol.data.game.entity.attribute;

import com.github.steveice10.mc.protocol.data.MagicValues;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class AttributeModifier {
    private final @NonNull ModifierType type;
    private final @NonNull UUID uuid;
    private final double amount;
    private final @NonNull ModifierOperation operation;

    public AttributeModifier(@NonNull ModifierType type, double amount, @NonNull ModifierOperation operation) {
        if(type == ModifierType.DYNAMIC) {
            throw new IllegalArgumentException("Cannot create a dynamic-typed modifier without a UUID.");
        }

        this.type = type;
        this.uuid = MagicValues.value(UUID.class, type);
        this.amount = amount;
        this.operation = operation;
    }

    public AttributeModifier(@NonNull UUID uuid, double amount, @NonNull ModifierOperation operation) {
        ModifierType type = ModifierType.DYNAMIC;
        try {
            type = MagicValues.key(ModifierType.class, uuid);
        } catch(IllegalArgumentException e) {
        }

        this.type = type;
        this.uuid = uuid;
        this.amount = amount;
        this.operation = operation;
    }
}
