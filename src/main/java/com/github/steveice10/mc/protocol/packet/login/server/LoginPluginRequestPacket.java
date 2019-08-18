package com.github.steveice10.mc.protocol.packet.login.server;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LoginPluginRequestPacket implements Packet {
    private int messageId;
    private @NonNull String channel;
    private @NonNull byte[] data;

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

    @Override
    public boolean isPriority() {
        return false;
    }
}
