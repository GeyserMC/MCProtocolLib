package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerEntityNBTUpdatePacket implements Packet {
    private int entityId;
    private CompoundTag tag;

    @SuppressWarnings("unused")
    private ServerEntityNBTUpdatePacket() {
    }

    public ServerEntityNBTUpdatePacket(int entityId, CompoundTag tag) {
        this.entityId = entityId;
        this.tag = tag;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public CompoundTag getTag() {
        return this.tag;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.tag = NetUtil.readNBT(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        NetUtil.writeNBT(out, this.tag);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
