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
public class ServerboundJigsawGeneratePacket implements Packet {
    private final @NonNull Position position;
    private final int levels;
    private final boolean keepJigsaws;

    public ServerboundJigsawGeneratePacket(NetInput in) throws IOException {
        this.position = Position.read(in);
        this.levels = in.readVarInt();
        this.keepJigsaws = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.position);
        out.writeVarInt(this.levels);
        out.writeBoolean(this.keepJigsaws);
    }
}
