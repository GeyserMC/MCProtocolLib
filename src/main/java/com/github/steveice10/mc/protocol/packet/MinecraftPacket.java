package com.github.steveice10.mc.protocol.packet;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.github.steveice10.packetlib.packet.Packet;

public abstract class MinecraftPacket implements Packet {
    @Override
    public boolean isPriority() {
        return false;
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
