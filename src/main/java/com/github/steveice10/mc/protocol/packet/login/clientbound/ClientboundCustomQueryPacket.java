package com.github.steveice10.mc.protocol.packet.login.clientbound;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundCustomQueryPacket implements Packet {
    private final int messageId;
    private final @NonNull String channel;
    private final @NonNull byte[] data;

    public ClientboundCustomQueryPacket(NetInput in) throws IOException {
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
