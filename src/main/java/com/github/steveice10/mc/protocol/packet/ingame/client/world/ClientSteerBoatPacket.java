package com.github.steveice10.mc.protocol.packet.ingame.client.world;

import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class ClientSteerBoatPacket implements Packet {

    private boolean unk1;
    private boolean unk2;

    @SuppressWarnings("unused")
    private ClientSteerBoatPacket() {
    }

    public ClientSteerBoatPacket(boolean unk1, boolean unk2) {
        this.unk1 = unk1;
        this.unk2 = unk2;
    }

    public boolean getUnknown1() {
        return this.unk1;
    }

    public boolean getUnknown2() {
        return this.unk2;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.unk1 = in.readBoolean();
        this.unk2 = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBoolean(this.unk1);
        out.writeBoolean(this.unk2);
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
