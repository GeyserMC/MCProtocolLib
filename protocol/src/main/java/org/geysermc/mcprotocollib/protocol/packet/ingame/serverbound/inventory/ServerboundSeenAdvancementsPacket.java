package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.AdvancementTabAction;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

@ToString
@EqualsAndHashCode
public class ServerboundSeenAdvancementsPacket implements MinecraftPacket {
    @Getter
    private final AdvancementTabAction action;
    private final @Nullable String tabId;

    public ServerboundSeenAdvancementsPacket() {
        this.action = AdvancementTabAction.CLOSED_SCREEN;
        this.tabId = null;
    }

    public ServerboundSeenAdvancementsPacket(String tabId) {
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

        assert this.tabId != null;
        return this.tabId;
    }

    public ServerboundSeenAdvancementsPacket(ByteBuf in) {
        this.action = AdvancementTabAction.from(MinecraftTypes.readVarInt(in));
        this.tabId = switch (this.action) {
            case CLOSED_SCREEN -> null;
            case OPENED_TAB -> MinecraftTypes.readString(in);
        };
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.action.ordinal());
        Consumer<@Nullable String> tabIdWriter = switch (this.action) {
            case CLOSED_SCREEN -> tabId -> {
            };
            case OPENED_TAB -> tabId -> MinecraftTypes.writeString(out, Objects.requireNonNull(tabId, "tabId must not be null for OPENED_TAB action"));
        };
        tabIdWriter.accept(this.tabId);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
