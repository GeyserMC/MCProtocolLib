package com.github.steveice10.mc.protocol.packet.ingame.server.window;

import java.io.IOException;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ServerOpenHorseWindowPacket extends MinecraftPacket {

    private int windowId;
    private int numberOfSlots;
    private int entityId;

    @SuppressWarnings("unused")
    private ServerOpenHorseWindowPacket() {
    }

    public ServerOpenHorseWindowPacket(int windowId, int numberOfSlots, int entityId) {
        this.windowId = windowId;
        this.numberOfSlots = numberOfSlots;
        this.entityId = entityId;
    }

    public int getWindowId() {
        return windowId;
    }

    public int getNumberOfSlots() {
        return numberOfSlots;
    }

    public int getEntityId() {
        return entityId;
    }

    @Override
    public void read(NetInput in) throws IOException {
        windowId = in.readByte();
        numberOfSlots = in.readVarInt();
        entityId = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(windowId);
        out.writeVarInt(numberOfSlots);
        out.writeInt(entityId);
    }
}
