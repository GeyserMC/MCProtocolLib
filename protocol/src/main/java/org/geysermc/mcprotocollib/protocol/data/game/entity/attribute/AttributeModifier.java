package org.geysermc.mcprotocollib.protocol.data.game.entity.attribute;

import lombok.Data;
import net.kyori.adventure.key.Key;

@Data
public class AttributeModifier {
    private final Key id;
    private final double amount;
    private final ModifierOperation operation;

    public AttributeModifier(Key id, double amount, ModifierOperation operation) {
        this.id = id;
        this.amount = amount;
        this.operation = operation;
    }
}
