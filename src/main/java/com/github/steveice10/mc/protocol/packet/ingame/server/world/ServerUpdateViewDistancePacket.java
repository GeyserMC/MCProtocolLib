package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ServerUpdateViewDistancePacket extends MinecraftPacket {

    private int viewDistance;

    @Override
    public void read(NetInput in) throws IOException {
        this.viewDistance = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.viewDistance);
    }
}
