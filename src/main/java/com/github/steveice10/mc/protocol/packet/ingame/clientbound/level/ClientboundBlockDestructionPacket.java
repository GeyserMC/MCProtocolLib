package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.BlockBreakStage;
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
public class ClientboundBlockDestructionPacket implements Packet {
    private final int breakerEntityId;
    private final @NonNull Position position;
    private final @NonNull BlockBreakStage stage;

    public ClientboundBlockDestructionPacket(NetInput in) throws IOException {
        this.breakerEntityId = in.readVarInt();
        this.position = Position.read(in);
        int stage = in.readUnsignedByte();
        if (stage >= 0 && stage < 10) {
            this.stage = BlockBreakStage.STAGES[stage];
        } else {
            this.stage = BlockBreakStage.RESET;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.breakerEntityId);
        Position.write(out, this.position);
        this.stage.write(out);
    }
}
