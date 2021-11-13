package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.inventory.AdvancementTabAction;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.IOException;

@ToString
@EqualsAndHashCode
public class ServerboundSeenAdvancementsPacket implements Packet {
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
     * @throws IllegalStateException if {@link #getAction()} is not {@link AdvancementTabAction#OPENED_TAB}.
     */
    public String getTabId() {
        if (this.action != AdvancementTabAction.OPENED_TAB) {
            throw new IllegalStateException("tabId is only set if action is " + AdvancementTabAction.OPENED_TAB
                    + " but it was " + this.action);
        }

        return this.tabId;
    }

    public ServerboundSeenAdvancementsPacket(NetInput in) throws IOException {
        this.action = MagicValues.key(AdvancementTabAction.class, in.readVarInt());
        switch (this.action) {
            case CLOSED_SCREEN:
                this.tabId = null;
                break;
            case OPENED_TAB:
                this.tabId = in.readString();
                break;
            default:
                throw new IOException("Unknown advancement tab action: " + this.action);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        switch (this.action) {
            case CLOSED_SCREEN:
                break;
            case OPENED_TAB:
                out.writeString(this.tabId);
                break;
            default:
                throw new IOException("Unknown advancement tab action: " + this.action);
        }
    }
}
