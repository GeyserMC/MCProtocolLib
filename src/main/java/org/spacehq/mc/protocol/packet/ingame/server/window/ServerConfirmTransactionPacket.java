package org.spacehq.mc.protocol.packet.ingame.server.window;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerConfirmTransactionPacket implements Packet {

    private int windowId;
    private int actionId;
    private boolean accepted;

    @SuppressWarnings("unused")
    private ServerConfirmTransactionPacket() {
    }

    public ServerConfirmTransactionPacket(int windowId, int actionId, boolean accepted) {
        this.windowId = windowId;
        this.actionId = actionId;
        this.accepted = accepted;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getActionId() {
        return this.actionId;
    }

    public boolean getAccepted() {
        return this.accepted;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.actionId = in.readShort();
        this.accepted = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.actionId);
        out.writeBoolean(this.accepted);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
