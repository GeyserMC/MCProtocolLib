package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

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
public class ClientboundSetChunkCacheRadiusPacket implements Packet {
    private final int viewDistance;

    public ClientboundSetChunkCacheRadiusPacket(NetInput in) throws IOException {
        this.viewDistance = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.viewDistance);
    }
}
