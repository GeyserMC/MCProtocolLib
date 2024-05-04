package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.advancement.Advancement;
import org.geysermc.mcprotocollib.protocol.data.game.advancement.Advancement.DisplayData;
import org.geysermc.mcprotocollib.protocol.data.game.advancement.Advancement.DisplayData.AdvancementType;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

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

    public ClientboundUpdateAdvancementsPacket(MinecraftByteBuf buf) {
        this.reset = buf.readBoolean();

        this.advancements = new Advancement[buf.readVarInt()];
        for (int i = 0; i < this.advancements.length; i++) {
            String id = buf.readString();
            String parentId = buf.readNullable(buf::readString);
            DisplayData displayData = null;
            if (buf.readBoolean()) {
                Component title = buf.readComponent();
                Component description = buf.readComponent();
                ItemStack icon = buf.readOptionalItemStack();
                AdvancementType advancementType = AdvancementType.from(buf.readVarInt());

                int flags = buf.readInt();
                boolean hasBackgroundTexture = (flags & FLAG_HAS_BACKGROUND_TEXTURE) != 0;
                boolean showToast = (flags & FLAG_SHOW_TOAST) != 0;
                boolean hidden = (flags & FLAG_HIDDEN) != 0;

                String backgroundTexture = hasBackgroundTexture ? buf.readString() : null;
                float posX = buf.readFloat();
                float posY = buf.readFloat();

                displayData = new DisplayData(title, description, icon, advancementType, showToast, hidden, posX, posY, backgroundTexture);
            }

            List<List<String>> requirements = new ArrayList<>();
            int requirementCount = buf.readVarInt();
            for (int j = 0; j < requirementCount; j++) {
                List<String> requirement = new ArrayList<>();
                int componentCount = buf.readVarInt();
                for (int k = 0; k < componentCount; k++) {
                    requirement.add(buf.readString());
                }

                requirements.add(requirement);
            }

            boolean sendTelemetryEvent = buf.readBoolean();

            this.advancements[i] = new Advancement(id, requirements, parentId, displayData, sendTelemetryEvent);
        }

        this.removedAdvancements = new String[buf.readVarInt()];
        for (int i = 0; i < this.removedAdvancements.length; i++) {
            this.removedAdvancements[i] = buf.readString();
        }

        this.progress = new HashMap<>();
        int progressCount = buf.readVarInt();
        for (int i = 0; i < progressCount; i++) {
            String advancementId = buf.readString();

            Map<String, Long> advancementProgress = new HashMap<>();
            int criterionCount = buf.readVarInt();
            for (int j = 0; j < criterionCount; j++) {
                String criterionId = buf.readString();
                long achievedDate = buf.readBoolean() ? buf.readLong() : -1;
                advancementProgress.put(criterionId, achievedDate);
            }

            this.progress.put(advancementId, advancementProgress);
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeBoolean(this.reset);

        buf.writeVarInt(this.advancements.length);
        for (Advancement advancement : this.advancements) {
            buf.writeString(advancement.getId());
            if (advancement.getParentId() != null) {
                buf.writeBoolean(true);
                buf.writeString(advancement.getParentId());
            } else {
                buf.writeBoolean(false);
            }

            DisplayData displayData = advancement.getDisplayData();
            if (displayData != null) {
                buf.writeBoolean(true);
                buf.writeComponent(displayData.getTitle());
                buf.writeComponent(displayData.getDescription());
                buf.writeOptionalItemStack(displayData.getIcon());
                buf.writeVarInt(displayData.getAdvancementType().ordinal());
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

                buf.writeInt(flags);

                if (backgroundTexture != null) {
                    buf.writeString(backgroundTexture);
                }

                buf.writeFloat(displayData.getPosX());
                buf.writeFloat(displayData.getPosY());
            } else {
                buf.writeBoolean(false);
            }

            buf.writeVarInt(advancement.getRequirements().size());
            for (List<String> requirement : advancement.getRequirements()) {
                buf.writeVarInt(requirement.size());
                for (String criterion : requirement) {
                    buf.writeString(criterion);
                }
            }

            buf.writeBoolean(advancement.isSendsTelemetryEvent());
        }

        buf.writeVarInt(this.removedAdvancements.length);
        for (String id : this.removedAdvancements) {
            buf.writeString(id);
        }

        buf.writeVarInt(this.progress.size());
        for (Map.Entry<String, Map<String, Long>> advancement : this.progress.entrySet()) {
            buf.writeString(advancement.getKey());
            Map<String, Long> advancementProgress = advancement.getValue();
            buf.writeVarInt(advancementProgress.size());
            for (Map.Entry<String, Long> criterion : advancementProgress.entrySet()) {
                buf.writeString(criterion.getKey());
                if (criterion.getValue() != -1) {
                    buf.writeBoolean(true);
                    buf.writeLong(criterion.getValue());
                } else {
                    buf.writeBoolean(false);
                }
            }
        }
    }
}
