package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement.DisplayData;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement.DisplayData.FrameType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerAdvancementsPacket extends MinecraftPacket {
    private boolean reset;
    private List<Advancement> advancements;
    private List<String> removedAdvancements;
    private Map<String, Map<String, Long>> progress;

    @SuppressWarnings("unused")
    private ServerAdvancementsPacket() {
    }

    public ServerAdvancementsPacket(boolean reset, List<Advancement> advancements, List<String> removedAdvancements, Map<String, Map<String, Long>> progress) {
        this.reset = reset;
        this.advancements = advancements;
        this.removedAdvancements = removedAdvancements;
        this.progress = progress;
    }

    public boolean doesReset() {
        return this.reset;
    }

    public List<Advancement> getAdvancements() {
        return this.advancements;
    }

    public List<String> getRemovedAdvancements() {
        return this.removedAdvancements;
    }

    public Map<String, Map<String, Long>> getProgress() {
        return this.progress;
    }

    public Map<String, Long> getProgress(String advancementId) {
        return getProgress().get(advancementId);
    }

    public Long getAchievedDate(String advancementId, String criterionId) {
        return getProgress(advancementId).get(criterionId);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.reset = in.readBoolean();

        this.advancements = new ArrayList<>();
        int advancementCount = in.readVarInt();
        for(int i = 0; i < advancementCount; i++) {
            String id = in.readString();
            String parentId = in.readBoolean() ? in.readString() : null;
            DisplayData displayData = null;
            if(in.readBoolean()) {
                String title = in.readString();
                String description = in.readString();
                ItemStack icon = NetUtil.readItem(in);
                FrameType frameType = MagicValues.key(FrameType.class, in.readVarInt());

                int flags = in.readInt();
                boolean hasBackgroundTexture = (flags & 0x1) != 0;
                boolean showToast = (flags & 0x2) != 0;
                boolean hidden = (flags & 0x4) != 0;

                String backgroundTexture = hasBackgroundTexture ? in.readString() : null;
                float posX = in.readFloat();
                float posY = in.readFloat();

                displayData = new DisplayData(title, description, icon, frameType, showToast, hidden, posX, posY, backgroundTexture);
            }

            List<String> criteria = new ArrayList<>();
            int criteriaCount = in.readVarInt();
            for(int j = 0; j < criteriaCount; j++) {
                criteria.add(in.readString());
            }

            List<List<String>> requirements = new ArrayList<>();
            int requirementCount = in.readVarInt();
            for(int j = 0; j < requirementCount; j++) {
                List<String> requirement = new ArrayList<>();
                int componentCount = in.readVarInt();
                for(int k = 0; k < componentCount; k++) {
                    requirement.add(in.readString());
                }

                requirements.add(requirement);
            }

            this.advancements.add(new Advancement(id, parentId, criteria, requirements, displayData));
        }

        this.removedAdvancements = new ArrayList<>();
        int removedCount = in.readVarInt();
        for(int i = 0; i < removedCount; i++) {
            this.removedAdvancements.add(in.readString());
        }

        this.progress = new HashMap<>();
        int progressCount = in.readVarInt();
        for(int i = 0; i < progressCount; i++) {
            String advancementId = in.readString();

            Map<String, Long> advancementProgress = new HashMap<>();
            int criterionCount = in.readVarInt();
            for(int j = 0; j < criterionCount; j++) {
                String criterionId = in.readString();
                Long achievedDate = in.readBoolean() ? in.readLong() : null;
                advancementProgress.put(criterionId, achievedDate);
            }

            this.progress.put(advancementId, advancementProgress);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBoolean(this.reset);

        out.writeVarInt(this.advancements.size());
        for(Advancement advancement : this.advancements) {
            out.writeString(advancement.getId());
            if(advancement.getParentId() != null) {
                out.writeBoolean(true);
                out.writeString(advancement.getParentId());
            } else {
                out.writeBoolean(false);
            }

            DisplayData displayData = advancement.getDisplayData();
            if(displayData != null) {
                out.writeBoolean(true);
                out.writeString(displayData.getTitle());
                out.writeString(displayData.getDescription());
                NetUtil.writeItem(out, displayData.getIcon());
                out.writeVarInt(MagicValues.value(Integer.class, displayData.getFrameType()));
                String backgroundTexture = displayData.getBackgroundTexture();

                int flags = 0;
                if(backgroundTexture != null) flags |= 0x1;
                if(displayData.doesShowToast()) flags |= 0x2;
                if(displayData.isHidden()) flags |= 0x4;
                out.writeInt(flags);

                if(backgroundTexture != null) {
                    out.writeString(backgroundTexture);
                }

                out.writeFloat(displayData.getPosX());
                out.writeFloat(displayData.getPosY());
            } else {
                out.writeBoolean(false);
            }

            out.writeVarInt(advancement.getCriteria().size());
            for(String criterion : advancement.getCriteria()) {
                out.writeString(criterion);
            }

            out.writeVarInt(advancement.getRequirements().size());
            for(List<String> requirement : advancement.getRequirements()) {
                out.writeVarInt(requirement.size());
                for(String criterion : requirement) {
                    out.writeString(criterion);
                }
            }
        }

        out.writeVarInt(this.removedAdvancements.size());
        for(String id : this.removedAdvancements) {
            out.writeString(id);
        }

        out.writeVarInt(this.progress.size());
        for(Map.Entry<String, Map<String, Long>> advancement : this.progress.entrySet()) {
            out.writeString(advancement.getKey());
            Map<String, Long> advancementProgress = advancement.getValue();
            out.writeVarInt(advancementProgress.size());
            for(Map.Entry<String, Long> criterion : advancementProgress.entrySet()) {
                out.writeString(criterion.getKey());
                if(criterion.getValue() != null) {
                    out.writeBoolean(true);
                    out.writeLong(criterion.getValue());
                } else {
                    out.writeBoolean(false);
                }
            }
        }
    }
}
