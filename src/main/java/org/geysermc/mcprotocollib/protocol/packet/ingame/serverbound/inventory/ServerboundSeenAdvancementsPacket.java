package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.AdvancementTabAction;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.IOException;

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

    public ServerboundSeenAdvancementsPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.action = AdvancementTabAction.from(helper.readVarInt(in));
        switch (this.action) {
            case CLOSED_SCREEN -> this.tabId = null;
            case OPENED_TAB -> this.tabId = helper.readString(in);
            default -> throw new IOException("Unknown advancement tab action: " + this.action);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.action.ordinal());
        switch (this.action) {
            case CLOSED_SCREEN -> {
            }
            case OPENED_TAB -> helper.writeString(out, this.tabId);
            default -> throw new IOException("Unknown advancement tab action: " + this.action);
        }
    }
}
