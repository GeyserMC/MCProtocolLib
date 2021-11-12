package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

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
public class ClientboundOpenSignEditorPacket implements Packet {
    private final @NonNull Position position;

    public ClientboundOpenSignEditorPacket(NetInput in) throws IOException {
        this.position = Position.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.position);
    }
}
