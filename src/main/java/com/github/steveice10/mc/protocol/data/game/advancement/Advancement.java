package com.github.steveice10.mc.protocol.data.game.advancement;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
public class Advancement {
    private final @NonNull String id;
    private final @NonNull String parentId;
    private final @NonNull List<String> criteria;
    private final @NonNull List<List<String>> requirements;
    private final DisplayData displayData;

    public Advancement(@NonNull String id, @NonNull String parentId, @NonNull List<String> criteria, @NonNull List<List<String>> requirements) {
        this(id, parentId, criteria, requirements, null);
    }

    @Data
    @AllArgsConstructor
    public static class DisplayData {
        private final @NonNull Message title;
        private final @NonNull Message description;
        private final @NonNull ItemStack icon;
        private final @NonNull FrameType frameType;
        private final boolean showToast;
        private final boolean hidden;
        private final float posX;
        private final float posY;
        private final String backgroundTexture;

        public DisplayData(@NonNull Message title, @NonNull Message description, @NonNull ItemStack icon, @NonNull FrameType frameType,
                           boolean showToast, boolean hidden, float posX, float posY) {
            this(title, description, icon, frameType, showToast, hidden, posX, posY, null);
        }

        public enum FrameType {
            TASK,
            CHALLENGE,
            GOAL;
        }
    }
}
