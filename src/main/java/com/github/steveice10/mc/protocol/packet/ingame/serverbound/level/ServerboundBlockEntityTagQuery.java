package com.github.steveice10.mc.protocol.packet.ingame.serverbound.level;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
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
public class ServerboundBlockEntityTagQuery implements Packet {
    private final int transactionId;
    private final @NonNull Position position;

    public ServerboundBlockEntityTagQuery(NetInput in) throws IOException {
        this.transactionId = in.readVarInt();
        this.position = Position.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.transactionId);
        Position.write(out, this.position);
    }
}
