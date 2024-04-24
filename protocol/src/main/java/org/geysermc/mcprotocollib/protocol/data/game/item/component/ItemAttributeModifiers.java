package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.ModifierOperation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ItemAttributeModifiers {
    private final List<Entry> modifiers;
    private final boolean showInTooltip;

    @Data
    @AllArgsConstructor
    public static class Entry {
        private final int attribute;
        private final AttributeModifier modifier;
        private final EquipmentSlotGroup slot;
    }

    @Data
    @AllArgsConstructor
    public static class AttributeModifier {
        private final UUID id;
        private final String name;
        private final double amount;
        private final ModifierOperation operation;
    }

    public enum EquipmentSlotGroup {
        ANY,
        MAIN_HAND,
        OFF_HAND,
        HAND,
        FEET,
        LEGS,
        CHEST,
        HEAD,
        ARMOR,
        BODY;

        private static final EquipmentSlotGroup[] VALUES = values();

        public static EquipmentSlotGroup from(int id) {
            return VALUES[id];
        }
    }
}
