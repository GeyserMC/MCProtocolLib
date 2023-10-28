package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement.DisplayData;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement.DisplayData.FrameType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateAdvancementsPacket implements MinecraftPacket {
    private static final int FLAG_HAS_BACKGROUND_TEXTURE = 0x01;
    private static final int FLAG_SHOW_TOAST = 0x02;
    private static final int FLAG_HIDDEN = 0x04;

    private final boolean reset;
    private final @NonNull Advancement[] advancements;
    private final @NonNull String[] removedAdvancements;
    private final @NonNull Map<String, Map<String, Long>> progress;

    public Map<String, Long> getProgress(@NonNull String advancementId) {
        return this.progress.get(advancementId);
    }

    public long getAchievedDate(@NonNull String advancementId, @NonNull String criterionId) {
        Map<String, Long> progress = this.getProgress(advancementId);
        if (progress == null || !progress.containsKey(criterionId)) {
            return -1;
        }

        return progress.get(criterionId);
    }

    public ClientboundUpdateAdvancementsPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.reset = in.readBoolean();

        this.advancements = new Advancement[helper.readVarInt(in)];
        for (int i = 0; i < this.advancements.length; i++) {
            String id = helper.readString(in);
            String parentId = helper.readNullable(in, helper::readString);
            DisplayData displayData = null;
            if (in.readBoolean()) {
                Component title = helper.readComponent(in);
                Component description = helper.readComponent(in);
                ItemStack icon = helper.readItemStack(in);
                FrameType frameType = FrameType.from(helper.readVarInt(in));

                int flags = in.readInt();
                boolean hasBackgroundTexture = (flags & FLAG_HAS_BACKGROUND_TEXTURE) != 0;
                boolean showToast = (flags & FLAG_SHOW_TOAST) != 0;
                boolean hidden = (flags & FLAG_HIDDEN) != 0;

                String backgroundTexture = hasBackgroundTexture ? helper.readString(in) : null;
                float posX = in.readFloat();
                float posY = in.readFloat();

                displayData = new DisplayData(title, description, icon, frameType, showToast, hidden, posX, posY, backgroundTexture);
            }

            List<List<String>> requirements = new ArrayList<>();
            int requirementCount = helper.readVarInt(in);
            for (int j = 0; j < requirementCount; j++) {
                List<String> requirement = new ArrayList<>();
                int componentCount = helper.readVarInt(in);
                for (int k = 0; k < componentCount; k++) {
                    requirement.add(helper.readString(in));
                }

                requirements.add(requirement);
            }

            boolean sendTelemetryEvent = in.readBoolean();

            this.advancements[i] = new Advancement(id, requirements, parentId, displayData, sendTelemetryEvent);
        }

        this.removedAdvancements = new String[helper.readVarInt(in)];
        for (int i = 0; i < this.removedAdvancements.length; i++) {
            this.removedAdvancements[i] = helper.readString(in);
        }

        this.progress = new HashMap<>();
        int progressCount = helper.readVarInt(in);
        for (int i = 0; i < progressCount; i++) {
            String advancementId = helper.readString(in);

            Map<String, Long> advancementProgress = new HashMap<>();
            int criterionCount = helper.readVarInt(in);
            for (int j = 0; j < criterionCount; j++) {
                String criterionId = helper.readString(in);
                long achievedDate = in.readBoolean() ? in.readLong() : -1;
                advancementProgress.put(criterionId, achievedDate);
            }

            this.progress.put(advancementId, advancementProgress);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeBoolean(this.reset);

        helper.writeVarInt(out, this.advancements.length);
        for (Advancement advancement : this.advancements) {
            helper.writeString(out, advancement.getId());
            if (advancement.getParentId() != null) {
                out.writeBoolean(true);
                helper.writeString(out, advancement.getParentId());
            } else {
                out.writeBoolean(false);
            }

            DisplayData displayData = advancement.getDisplayData();
            if (displayData != null) {
                out.writeBoolean(true);
                helper.writeComponent(out, displayData.getTitle());
                helper.writeComponent(out, displayData.getDescription());
                helper.writeItemStack(out, displayData.getIcon());
                helper.writeVarInt(out, displayData.getFrameType().ordinal());
                String backgroundTexture = displayData.getBackgroundTexture();

                int flags = 0;
                if (backgroundTexture != null) {
                    flags |= FLAG_HAS_BACKGROUND_TEXTURE;
                }

                if (displayData.isShowToast()) {
                    flags |= FLAG_SHOW_TOAST;
                }

                if (displayData.isHidden()) {
                    flags |= FLAG_HIDDEN;
                }

                out.writeInt(flags);

                if (backgroundTexture != null) {
                    helper.writeString(out, backgroundTexture);
                }

                out.writeFloat(displayData.getPosX());
                out.writeFloat(displayData.getPosY());
            } else {
                out.writeBoolean(false);
            }

            helper.writeVarInt(out, advancement.getRequirements().size());
            for (List<String> requirement : advancement.getRequirements()) {
                helper.writeVarInt(out, requirement.size());
                for (String criterion : requirement) {
                    helper.writeString(out, criterion);
                }
            }

            out.writeBoolean(advancement.isSendsTelemetryEvent());
        }

        helper.writeVarInt(out, this.removedAdvancements.length);
        for (String id : this.removedAdvancements) {
            helper.writeString(out, id);
        }

        helper.writeVarInt(out, this.progress.size());
        for (Map.Entry<String, Map<String, Long>> advancement : this.progress.entrySet()) {
            helper.writeString(out, advancement.getKey());
            Map<String, Long> advancementProgress = advancement.getValue();
            helper.writeVarInt(out, advancementProgress.size());
            for (Map.Entry<String, Long> criterion : advancementProgress.entrySet()) {
                helper.writeString(out, criterion.getKey());
                if (criterion.getValue() != -1) {
                    out.writeBoolean(true);
                    out.writeLong(criterion.getValue());
                } else {
                    out.writeBoolean(false);
                }
            }
        }
    }
}
