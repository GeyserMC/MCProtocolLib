package com.github.steveice10.mc.protocol.packet.status.serverbound;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

@Data
@NoArgsConstructor
public class ServerboundStatusRequestPacket implements Packet {

    public ServerboundStatusRequestPacket(NetInput in) {
    }

    @Override
    public void write(NetOutput out) throws IOException {
    }
}
