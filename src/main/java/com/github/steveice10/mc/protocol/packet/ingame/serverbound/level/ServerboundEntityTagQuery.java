package com.github.steveice10.mc.protocol.packet.ingame.serverbound.level;

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
public class ServerboundEntityTagQuery implements Packet {
    private final int transactionId;
    private final int entityId;

    public ServerboundEntityTagQuery(NetInput in) throws IOException {
        this.transactionId = in.readVarInt();
        this.entityId = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.transactionId);
        out.writeVarInt(this.entityId);
    }
}
