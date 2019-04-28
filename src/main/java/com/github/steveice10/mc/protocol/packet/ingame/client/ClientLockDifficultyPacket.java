package com.github.steveice10.mc.protocol.packet.ingame.client;

import java.io.IOException;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ClientLockDifficultyPacket extends MinecraftPacket {

    private boolean locked;

    public ClientLockDifficultyPacket() {
    }

    public ClientLockDifficultyPacket(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.locked = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBoolean(this.locked);
    }
}
