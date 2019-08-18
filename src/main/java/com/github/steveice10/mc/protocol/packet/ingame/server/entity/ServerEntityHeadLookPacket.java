package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerEntityHeadLookPacket implements Packet {
    private int entityId;
    private float headYaw;

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.headYaw = in.readByte() * 360 / 256f;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte((byte) (this.headYaw * 256 / 360));
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
