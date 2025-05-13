package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.ModifierOperation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@With
public class ItemAttributeModifiers {
    private final List<Entry> modifiers;

    public ItemAttributeModifiers(List<Entry> modifiers) {
        this.modifiers = List.copyOf(modifiers);
    }

    @Data
    @Builder(toBuilder = true)
    @AllArgsConstructor
    public static class Entry {
        private final int attribute;
        private final AttributeModifier modifier;
        private final EquipmentSlotGroup slot;
        private final Display display;
    }

    @Data
    @Builder(toBuilder = true)
    @AllArgsConstructor
    public static class AttributeModifier {
        private final Key id;
        private final double amount;
        private final ModifierOperation operation;
    }

    @Data
    @Builder(toBuilder = true)
    @AllArgsConstructor
    public static class Display {
        DisplayType type;
        @Nullable Component component;
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
        BODY,
        SADDLE;

        private static final EquipmentSlotGroup[] VALUES = values();

        public static EquipmentSlotGroup from(int id) {
            return VALUES[id];
        }
    }

    public enum DisplayType {
        DEFAULT,
        HIDDEN,
        OVERRIDE;

        private static final DisplayType[] VALUES = values();

        public static DisplayType from(int id) {
            return id >= 0 && id < VALUES.length ? VALUES[id] : VALUES[0];
        }
    }
}
