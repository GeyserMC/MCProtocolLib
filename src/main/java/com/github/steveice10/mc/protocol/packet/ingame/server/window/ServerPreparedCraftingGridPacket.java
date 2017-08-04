package com.github.steveice10.mc.protocol.packet.ingame.server.window;

import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class ServerPreparedCraftingGridPacket implements Packet {

    private int windowId;
    private int recipeId;

    @SuppressWarnings("unused")
    private ServerPreparedCraftingGridPacket() {
    }

    public ServerPreparedCraftingGridPacket(int windowId, int recipeId) {
        this.windowId = windowId;
        this.recipeId = recipeId;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getRecipeId() {
        return this.recipeId;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readByte();
        this.recipeId = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeVarInt(this.recipeId);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}
