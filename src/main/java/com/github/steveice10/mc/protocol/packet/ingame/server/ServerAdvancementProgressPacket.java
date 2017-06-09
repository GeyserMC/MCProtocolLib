package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class ServerAdvancementProgressPacket implements Packet {
    private String id;

    @SuppressWarnings("unused")
    private ServerAdvancementProgressPacket() {
    }

    public ServerAdvancementProgressPacket(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public void read(NetInput in) throws IOException {
        if (in.readBoolean()) {
            this.id = in.readString();
        } else {
            this.id = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        if (this.id != null) {
            out.writeBoolean(true);
            out.writeString(this.id);
        } else {
            out.writeBoolean(false);
        }
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
