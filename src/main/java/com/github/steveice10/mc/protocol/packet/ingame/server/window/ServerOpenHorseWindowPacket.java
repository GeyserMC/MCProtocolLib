package com.github.steveice10.mc.protocol.packet.ingame.server.window;

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
public class ServerOpenHorseWindowPacket implements Packet {
    private int windowId;
    private int numberOfSlots;
    private int entityId;

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readByte();
        this.numberOfSlots = in.readVarInt();
        this.entityId = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeVarInt(this.numberOfSlots);
        out.writeInt(this.entityId);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
