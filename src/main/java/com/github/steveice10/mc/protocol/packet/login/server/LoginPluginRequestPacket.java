package com.github.steveice10.mc.protocol.packet.login.server;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class LoginPluginRequestPacket extends MinecraftPacket {
    private int messageId;
    private String channel;
    private byte[] data;

    @SuppressWarnings("unused")
    private LoginPluginRequestPacket() {
    }

    public LoginPluginRequestPacket(int messageId, String channel, byte[] data) {
        this.messageId = messageId;
        this.channel = channel;
        this.data = data;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public String getChannel() {
        return this.channel;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.messageId = in.readVarInt();
        this.channel = in.readString();
        this.data = in.readBytes(in.available());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.messageId);
        out.writeString(this.channel);
        out.writeBytes(this.data);
    }
}
