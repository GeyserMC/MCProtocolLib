package com.github.steveice10.mc.protocol.data.game.advancement;

import com.github.steveice10.mc.protocol.data.game.item.ItemStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
public class Advancement {
    private final @NonNull String id;
    private final @NonNull List<List<String>> requirements;
    private final String parentId;
    private final DisplayData displayData;
    private final boolean sendsTelemetryEvent;

    public Advancement(@NonNull String id, @NonNull List<List<String>> requirements, boolean sendsTelemetryEvent) {
        this(id, requirements, null, null, sendsTelemetryEvent);
    }

    public Advancement(@NonNull String id, @NonNull List<List<String>> requirements, String parentId, boolean sendsTelemetryEvent) {
        this(id, requirements, parentId, null, sendsTelemetryEvent);
    }

    public Advancement(@NonNull String id, @NonNull List<List<String>> requirements, DisplayData displayData, boolean sendsTelemetryEvent) {
        this(id, requirements, null, displayData, sendsTelemetryEvent);
    }

    @Data
    @AllArgsConstructor
    public static class DisplayData {
        private final @NonNull Component title;
        private final @NonNull Component description;
        private final @Nullable ItemStack icon;
        private final @NonNull AdvancementType advancementType;
        private final boolean showToast;
        private final boolean hidden;
        private final float posX;
        private final float posY;
        private final @Nullable String backgroundTexture;

        public DisplayData(@NonNull Component title, @NonNull Component description, @Nullable ItemStack icon, @NonNull AdvancementType advancementType,
                           boolean showToast, boolean hidden, float posX, float posY) {
            this(title, description, icon, advancementType, showToast, hidden, posX, posY, null);
        }

        public enum AdvancementType {
            TASK,
            CHALLENGE,
            GOAL;

            private static final AdvancementType[] VALUES = values();

            public static AdvancementType from(int id) {
                return VALUES[id];
            }
        }
    }
}
