package org.geysermc.mcprotocollib.protocol.data.game.advancement;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
public class Advancement {
    private final String id;
    private final List<List<String>> requirements;
    private final String parentId;
    private final DisplayData displayData;
    private final boolean sendsTelemetryEvent;
    private final float posX;
    private final float posY;

    public Advancement(String id, List<List<String>> requirements, boolean sendsTelemetryEvent) {
        this(id, requirements, null, null, sendsTelemetryEvent, 0.0F, 0.0F);
    }

    public Advancement(String id, List<List<String>> requirements, String parentId, boolean sendsTelemetryEvent) {
        this(id, requirements, parentId, null, sendsTelemetryEvent, 0.0F, 0.0F);
    }

    public Advancement(String id, List<List<String>> requirements, DisplayData displayData, boolean sendsTelemetryEvent) {
        this(id, requirements, null, displayData, sendsTelemetryEvent, 0.0F, 0.0F);
    }

    @Data
    @AllArgsConstructor
    public static class DisplayData {
        private final Component title;
        private final Component description;
        private final ItemStack icon;
        private final AdvancementType advancementType;
        private final boolean showToast;
        private final boolean hidden;
        private final @Nullable String backgroundTexture;

        public DisplayData(Component title, Component description, ItemStack icon, AdvancementType advancementType,
                           boolean showToast, boolean hidden) {
            this(title, description, icon, advancementType, showToast, hidden, null);
        }

        public enum AdvancementType {
            TASK,
            CHALLENGE,
            GOAL;

            private static final AdvancementType[] VALUES = values();

            public static AdvancementType from(int id) {
                return id >= 0 & id < VALUES.length ? VALUES[id] : VALUES[0];
            }
        }
    }
}
