package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundCustomQueryPacket implements Packet {
    private final int messageId;
    private final byte[] data;

    public ServerboundCustomQueryPacket(int messageId) {
        this(messageId, null);
    }

    public ServerboundCustomQueryPacket(NetInput in) throws IOException {
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
