package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.window.AdvancementTabAction;
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
public class ClientAdvancementTabPacket implements Packet {
    @Getter
    private @NonNull AdvancementTabAction action;
    private String tabId;

    public ClientAdvancementTabPacket() {
        this.action = AdvancementTabAction.CLOSED_SCREEN;
    }

    public ClientAdvancementTabPacket(@NonNull String tabId) {
        this.action = AdvancementTabAction.OPENED_TAB;
        this.tabId = tabId;
    }

    public String getTabId() {
        if (this.action != AdvancementTabAction.OPENED_TAB) {
            throw new IllegalStateException("tabId is only set if action is " + AdvancementTabAction.OPENED_TAB
                    + " but it was " + this.action);
        }

        return this.tabId;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.action = MagicValues.key(AdvancementTabAction.class, in.readVarInt());
        switch (this.action) {
            case CLOSED_SCREEN:
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
