package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement.DisplayData;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement.DisplayData.FrameType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerAdvancementsPacket implements Packet {
    private static final int FLAG_HAS_BACKGROUND_TEXTURE = 0x01;
    private static final int FLAG_SHOW_TOAST = 0x02;
    private static final int FLAG_HIDDEN = 0x04;

    private boolean reset;
    private @NonNull Advancement[] advancements;
    private @NonNull String[] removedAdvancements;
    private @NonNull Map<String, Map<String, Long>> progress;

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

    @Override
    public void read(NetInput in) throws IOException {
        this.reset = in.readBoolean();

        this.advancements = new Advancement[in.readVarInt()];
        for (int i = 0; i < this.advancements.length; i++) {
            String id = in.readString();
            String parentId = in.readBoolean() ? in.readString() : null;
            DisplayData displayData = null;
            if (in.readBoolean()) {
                Component title = DefaultComponentSerializer.get().deserialize(in.readString());
                Component description = DefaultComponentSerializer.get().deserialize(in.readString());
                ItemStack icon = ItemStack.read(in);
                FrameType frameType = MagicValues.key(FrameType.class, in.readVarInt());

                int flags = in.readInt();
                boolean hasBackgroundTexture = (flags & FLAG_HAS_BACKGROUND_TEXTURE) != 0;
                boolean showToast = (flags & FLAG_SHOW_TOAST) != 0;
                boolean hidden = (flags & FLAG_HIDDEN) != 0;

                String backgroundTexture = hasBackgroundTexture ? in.readString() : null;
                float posX = in.readFloat();
                float posY = in.readFloat();

                displayData = new DisplayData(title, description, icon, frameType, showToast, hidden, posX, posY, backgroundTexture);
            }

            List<String> criteria = new ArrayList<>();
            int criteriaCount = in.readVarInt();
            for (int j = 0; j < criteriaCount; j++) {
                criteria.add(in.readString());
            }

            List<List<String>> requirements = new ArrayList<>();
            int requirementCount = in.readVarInt();
            for (int j = 0; j < requirementCount; j++) {
                List<String> requirement = new ArrayList<>();
                int componentCount = in.readVarInt();
                for (int k = 0; k < componentCount; k++) {
                    requirement.add(in.readString());
                }

                requirements.add(requirement);
            }

            this.advancements[i] = new Advancement(id, criteria, requirements, parentId, displayData);
        }

        this.removedAdvancements = new String[in.readVarInt()];
        for (int i = 0; i < this.removedAdvancements.length; i++) {
            this.removedAdvancements[i] = in.readString();
        }

        this.progress = new HashMap<>();
        int progressCount = in.readVarInt();
        for (int i = 0; i < progressCount; i++) {
            String advancementId = in.readString();

            Map<String, Long> advancementProgress = new HashMap<>();
            int criterionCount = in.readVarInt();
            for (int j = 0; j < criterionCount; j++) {
                String criterionId = in.readString();
                long achievedDate = in.readBoolean() ? in.readLong() : -1;
                advancementProgress.put(criterionId, achievedDate);
            }

            this.progress.put(advancementId, advancementProgress);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBoolean(this.reset);

        out.writeVarInt(this.advancements.length);
        for (Advancement advancement : this.advancements) {
            out.writeString(advancement.getId());
            if (advancement.getParentId() != null) {
                out.writeBoolean(true);
                out.writeString(advancement.getParentId());
            } else {
                out.writeBoolean(false);
            }

            DisplayData displayData = advancement.getDisplayData();
            if (displayData != null) {
                out.writeBoolean(true);
                out.writeString(DefaultComponentSerializer.get().serialize(displayData.getTitle()));
                out.writeString(DefaultComponentSerializer.get().serialize(displayData.getDescription()));
                ItemStack.write(out, displayData.getIcon());
                out.writeVarInt(MagicValues.value(Integer.class, displayData.getFrameType()));
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
                    out.writeString(backgroundTexture);
                }

                out.writeFloat(displayData.getPosX());
                out.writeFloat(displayData.getPosY());
            } else {
                out.writeBoolean(false);
            }

            out.writeVarInt(advancement.getCriteria().size());
            for (String criterion : advancement.getCriteria()) {
                out.writeString(criterion);
            }

            out.writeVarInt(advancement.getRequirements().size());
            for (List<String> requirement : advancement.getRequirements()) {
                out.writeVarInt(requirement.size());
                for (String criterion : requirement) {
                    out.writeString(criterion);
                }
            }
        }

        out.writeVarInt(this.removedAdvancements.length);
        for (String id : this.removedAdvancements) {
            out.writeString(id);
        }

        out.writeVarInt(this.progress.size());
        for (Map.Entry<String, Map<String, Long>> advancement : this.progress.entrySet()) {
            out.writeString(advancement.getKey());
            Map<String, Long> advancementProgress = advancement.getValue();
            out.writeVarInt(advancementProgress.size());
            for (Map.Entry<String, Long> criterion : advancementProgress.entrySet()) {
                out.writeString(criterion.getKey());
                if (criterion.getValue() != -1) {
                    out.writeBoolean(true);
                    out.writeLong(criterion.getValue());
                } else {
                    out.writeBoolean(false);
                }
            }
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
