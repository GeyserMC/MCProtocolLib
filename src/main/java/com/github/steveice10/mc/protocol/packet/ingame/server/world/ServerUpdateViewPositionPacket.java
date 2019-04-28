package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ServerUpdateViewPositionPacket extends MinecraftPacket {

    private int chunkX;
    private int chunkZ;

    public ServerUpdateViewPositionPacket() {
    }

    public ServerUpdateViewPositionPacket(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.chunkX = in.readVarInt();
        this.chunkZ = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.chunkX);
        out.writeVarInt(this.chunkZ);
    }
}
