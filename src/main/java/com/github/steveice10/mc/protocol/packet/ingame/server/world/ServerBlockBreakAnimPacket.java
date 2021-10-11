package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.UnmappedValueException;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.BlockBreakStage;
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
public class ServerBlockBreakAnimPacket implements Packet {
    private int breakerEntityId;
    private @NonNull Position position;
    private @NonNull BlockBreakStage stage;

    @Override
    public void read(NetInput in) throws IOException {
        this.breakerEntityId = in.readVarInt();
        this.position = Position.read(in);
        try {
            this.stage = MagicValues.key(BlockBreakStage.class, in.readUnsignedByte());
        } catch (UnmappedValueException e) {
            this.stage = BlockBreakStage.RESET;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.breakerEntityId);
        Position.write(out, this.position);
        out.writeByte(MagicValues.value(Integer.class, this.stage));
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
