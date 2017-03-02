package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class ServerPlayerListDataPacket implements Packet {
    private Message header;
    private Message footer;

    @SuppressWarnings("unused")
    private ServerPlayerListDataPacket() {
    }

    public ServerPlayerListDataPacket(Message header, Message footer) {
        this.header = header;
        this.footer = footer;
    }

    public Message getHeader() {
        return this.header;
    }

    public Message getFooter() {
        return this.footer;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.header = Message.fromString(in.readString());
        this.footer = Message.fromString(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.header.toJsonString());
        out.writeString(this.footer.toJsonString());
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
