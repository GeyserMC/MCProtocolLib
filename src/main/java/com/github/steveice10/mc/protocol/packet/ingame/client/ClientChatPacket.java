package com.github.steveice10.mc.protocol.packet.ingame.client;

import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class ClientChatPacket implements Packet {

    private String message;

    @SuppressWarnings("unused")
    private ClientChatPacket() {
    }

    public ClientChatPacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.message = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.message);
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
