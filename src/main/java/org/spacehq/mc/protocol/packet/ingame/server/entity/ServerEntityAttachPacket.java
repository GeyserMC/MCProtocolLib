package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerEntityAttachPacket implements Packet {

    private int entityId;
    private int attachedToId;
    private boolean leash;

    @SuppressWarnings("unused")
    private ServerEntityAttachPacket() {
    }

    public ServerEntityAttachPacket(int entityId, int attachedToId, boolean leash) {
        this.entityId = entityId;
        this.attachedToId = attachedToId;
        this.leash = leash;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int getAttachedToId() {
        return this.attachedToId;
    }

    public boolean getLeash() {
        return this.leash;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readInt();
        this.attachedToId = in.readInt();
        this.leash = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.entityId);
        out.writeInt(this.attachedToId);
        out.writeBoolean(this.leash);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
