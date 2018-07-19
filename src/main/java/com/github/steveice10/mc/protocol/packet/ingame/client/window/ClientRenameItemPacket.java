package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ClientRenameItemPacket extends MinecraftPacket {
    private String name;

    @SuppressWarnings("unused")
    private ClientRenameItemPacket() {
    }

    public ClientRenameItemPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.name = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.name);
    }
}
