package com.github.steveice10.mc.protocol.data.game.advancement;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import net.kyori.adventure.text.Component;

import java.util.List;

@Data
@AllArgsConstructor
public class Advancement {
    private final @NonNull String id;
    private final @NonNull List<String> criteria;
    private final @NonNull List<List<String>> requirements;
    private final String parentId;
    private final DisplayData displayData;

    public Advancement(@NonNull String id, @NonNull List<String> criteria, @NonNull List<List<String>> requirements) {
        this(id, criteria, requirements, null, null);
    }

    public Advancement(@NonNull String id, @NonNull List<String> criteria, @NonNull List<List<String>> requirements, String parentId) {
        this(id, criteria, requirements, parentId, null);
    }

    public Advancement(@NonNull String id, @NonNull List<String> criteria, @NonNull List<List<String>> requirements, DisplayData displayData) {
        this(id, criteria, requirements, null, displayData);
    }

    @Data
    @AllArgsConstructor
    public static class DisplayData {
        private final @NonNull Component title;
        private final @NonNull Component description;
        private final ItemStack icon;
        private final @NonNull FrameType frameType;
        private final boolean showToast;
        private final boolean hidden;
        private final float posX;
        private final float posY;
        private final String backgroundTexture;

        public DisplayData(@NonNull Component title, @NonNull Component description, ItemStack icon, @NonNull FrameType frameType,
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
