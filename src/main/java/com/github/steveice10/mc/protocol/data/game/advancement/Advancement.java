package com.github.steveice10.mc.protocol.data.game.advancement;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Advancement(@NotNull String id,
                          @NotNull List<List<String>> requirements,
                          String parentId, DisplayData displayData,
                          boolean sendsTelemetryEvent) {
    public Advancement(@NotNull String id, @NotNull List<List<String>> requirements, boolean sendsTelemetryEvent) {
        this(id, requirements, null, null, sendsTelemetryEvent);
    }

    public Advancement(@NotNull String id, @NotNull List<List<String>> requirements, String parentId, boolean sendsTelemetryEvent) {
        this(id, requirements, parentId, null, sendsTelemetryEvent);
    }

    public Advancement(@NotNull String id, @NotNull List<List<String>> requirements, DisplayData displayData, boolean sendsTelemetryEvent) {
        this(id, requirements, null, displayData, sendsTelemetryEvent);
    }

    public record DisplayData(@NotNull Component title, @NotNull Component description,
                              ItemStack icon, @NotNull FrameType frameType,
                              boolean showToast, boolean hidden,
                              float posX, float posY,
                              String backgroundTexture) {
        public DisplayData(@NotNull Component title, @NotNull Component description, ItemStack icon, @NotNull FrameType frameType,
                           boolean showToast, boolean hidden, float posX, float posY) {
            this(title, description, icon, frameType, showToast, hidden, posX, posY, null);
        }

        public enum FrameType {
            TASK,
            CHALLENGE,
            GOAL;

            private static final FrameType[] VALUES = values();

            public static FrameType from(int id) {
                return VALUES[id];
            }
        }
    }
}
