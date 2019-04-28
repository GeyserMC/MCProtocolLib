package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

// TODO: Implement
public class ServerUpdateLightPacket extends MinecraftPacket {

    private byte[] data;

    public ServerUpdateLightPacket() {
    }

    @Deprecated
    public ServerUpdateLightPacket(byte[] data) {
        this.data = data;
    }

    @Deprecated
    public byte[] getData() {
        return data;
    }

    @Deprecated
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void read(NetInput in) throws IOException {
        data = in.readBytes(in.available());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBytes(data);
    }
}
