package com.github.steveice10.mc.protocol.packet.ingame.server.world.border;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerSetBorderWarningDistancePacket implements Packet {
    private int warningBlocks;

    @Override
    public void read(NetInput in) throws IOException {
        this.warningBlocks = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.warningBlocks);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
