package com.github.steveice10.mc.protocol.packet.login.client;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class LoginPluginResponsePacket extends MinecraftPacket {
    private int messageId;
    private byte[] data;

    @SuppressWarnings("unused")
    private LoginPluginResponsePacket() {
    }

    public LoginPluginResponsePacket(int messageId) {
        this.messageId = messageId;
    }

    public LoginPluginResponsePacket(int messageId, byte[] data) {
        this.messageId = messageId;
        this.data = data;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.messageId = in.readVarInt();
        if (in.readBoolean()) {
            this.data = in.readBytes(in.available());
        } else {
            this.data = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.messageId);
        if (data != null) {
            out.writeBoolean(true);
            out.writeBytes(this.data);
        } else {
            out.writeBoolean(false);
        }
    }
}
