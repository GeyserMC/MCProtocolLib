package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.AdvancementTabAction;

import java.util.function.Consumer;

@ToString
@EqualsAndHashCode
public class ServerboundSeenAdvancementsPacket implements MinecraftPacket {
    @Getter
    private final @NonNull AdvancementTabAction action;
    private final String tabId;

    public ServerboundSeenAdvancementsPacket() {
        this.action = AdvancementTabAction.CLOSED_SCREEN;
        this.tabId = null;
    }

    public ServerboundSeenAdvancementsPacket(@NonNull String tabId) {
        this.action = AdvancementTabAction.OPENED_TAB;
        this.tabId = tabId;
    }

    /**
     * @throws IllegalStateException if #getAction() is not {@link AdvancementTabAction#OPENED_TAB}.
     */
    public String getTabId() {
        if (this.action != AdvancementTabAction.OPENED_TAB) {
            throw new IllegalStateException("tabId is only set if action is " + AdvancementTabAction.OPENED_TAB
                    + " but it was " + this.action);
        }

        return this.tabId;
    }

    public ServerboundSeenAdvancementsPacket(MinecraftByteBuf buf) {
        this.action = AdvancementTabAction.from(buf.readVarInt());
        this.tabId = switch (this.action) {
            case CLOSED_SCREEN -> null;
            case OPENED_TAB -> buf.readString();
        };
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.action.ordinal());
        Consumer<String> tabIdWriter = switch (this.action) {
            case CLOSED_SCREEN -> tabId -> {
            };
            case OPENED_TAB -> tabId -> buf.writeString(tabId);
        };
        tabIdWriter.accept(this.tabId);
    }
}
