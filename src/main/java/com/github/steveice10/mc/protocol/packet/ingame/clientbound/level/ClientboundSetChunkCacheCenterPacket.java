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
public class ClientboundSetChunkCacheCenterPacket implements Packet {
    private final int chunkX;
    private final int chunkZ;

    public ClientboundSetChunkCacheCenterPacket(NetInput in) throws IOException {
        this.chunkX = in.readVarInt();
        this.chunkZ = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.chunkX);
        out.writeVarInt(this.chunkZ);
    }
}
