package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockChangeRecord;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientboundBlockUpdatePacket implements Packet {
    private @NonNull BlockChangeRecord record;

    @Override
    public void read(NetInput in) throws IOException {
        this.record = new BlockChangeRecord(Position.read(in), in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.record.getPosition());
        out.writeVarInt(this.record.getBlock());
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
