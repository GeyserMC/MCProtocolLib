package com.github.steveice10.mc.protocol.packet.login.client;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginPluginResponsePacket implements Packet {
    private int messageId;
    private byte[] data;

    public LoginPluginResponsePacket(int messageId) {
        this(messageId, null);
    }

    public LoginPluginResponsePacket(int messageId, byte[] data) {
        this.messageId = messageId;
        this.data = data;
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
