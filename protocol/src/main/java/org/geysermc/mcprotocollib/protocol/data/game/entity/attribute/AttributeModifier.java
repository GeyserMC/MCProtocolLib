package org.geysermc.mcprotocollib.protocol.data.game.entity.attribute;

import lombok.Data;
import lombok.NonNull;
import net.kyori.adventure.key.Key;

import java.util.UUID;

@Data
public class AttributeModifier {
    private final @NonNull Key id;
    private final double amount;
    private final @NonNull ModifierOperation operation;

    public AttributeModifier(@NonNull Key id, double amount, @NonNull ModifierOperation operation) {
        this.id = id;
        this.amount = amount;
        this.operation = operation;
    }
}
