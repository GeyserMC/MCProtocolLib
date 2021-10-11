package com.github.steveice10.mc.protocol.data.game.entity.attribute;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.UnmappedValueException;
import lombok.Data;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

@Data
public class AttributeModifier {
    private final ModifierType type;
    private final @NonNull UUID uuid;
    private final double amount;
    private final @NonNull ModifierOperation operation;

    public AttributeModifier(@NonNull ModifierType type, double amount, @NonNull ModifierOperation operation) {
        this.type = type;
        this.uuid = MagicValues.value(UUID.class, type);
        this.amount = amount;
        this.operation = operation;
    }

    public AttributeModifier(@NonNull UUID uuid, double amount, @NonNull ModifierOperation operation) {
        ModifierType type = null;
        try {
            type = MagicValues.key(ModifierType.class, uuid);
        } catch (UnmappedValueException e) {
        }

        this.type = type;
        this.uuid = uuid;
        this.amount = amount;
        this.operation = operation;
    }

    public Optional<ModifierType> getType() {
        return Optional.ofNullable(this.type);
    }
}
