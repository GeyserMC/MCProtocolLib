package org.geysermc.mcprotocollib.protocol.data.game.entity.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Attribute {
    private final AttributeType type;
    private final double value;
    private final List<AttributeModifier> modifiers;

    public Attribute(AttributeType type) {
        this(type, 0);
    }

    public Attribute(AttributeType type, double value) {
        this(type, value, new ArrayList<>());
    }
}
