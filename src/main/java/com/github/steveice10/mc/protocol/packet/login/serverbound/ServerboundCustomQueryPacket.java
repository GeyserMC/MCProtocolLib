package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerboundCustomQueryPacket implements Packet {
    private int messageId;
    private byte[] data;

    public ServerboundCustomQueryPacket(int messageId) {
        this(messageId, null);
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
