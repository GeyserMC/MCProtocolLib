package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ServerDeclareRecipesPacket extends MinecraftPacket {
    private byte[] data;

    @SuppressWarnings("unused")
    private ServerDeclareRecipesPacket() {
    }

    @Deprecated // This packet isn't fully implemented, please send a PR if you need to use it
    public ServerDeclareRecipesPacket(byte[] data) {
        this.data = data;
    }

    @Deprecated // This packet isn't fully implemented, please send a PR if you need to use it
    public byte[] getData() {
        return data;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.data = in.readBytes(in.available());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBytes(this.data);
    }
}
