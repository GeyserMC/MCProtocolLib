package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockChangeEntry;
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
public class ClientboundBlockUpdatePacket implements Packet {
    private final @NonNull BlockChangeEntry entry;

    public ClientboundBlockUpdatePacket(NetInput in) throws IOException {
        this.entry = new BlockChangeEntry(Position.read(in), in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.entry.getPosition());
        out.writeVarInt(this.entry.getBlock());
    }
}
