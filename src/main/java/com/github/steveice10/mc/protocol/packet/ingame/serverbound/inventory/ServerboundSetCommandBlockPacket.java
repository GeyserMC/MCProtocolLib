package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.level.block.CommandBlockMode;
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
public class ServerboundSetCommandBlockPacket implements Packet {
    private static final int FLAG_TRACK_OUTPUT = 0x01;
    private static final int FLAG_CONDITIONAL = 0x02;
    private static final int FLAG_AUTOMATIC = 0x04;

    private final @NonNull Position position;
    private final @NonNull String command;
    private final @NonNull CommandBlockMode mode;
    private final boolean doesTrackOutput;
    private final boolean conditional;
    private final boolean automatic;

    public ServerboundSetCommandBlockPacket(NetInput in) throws IOException {
        this.position = Position.read(in);
        this.command = in.readString();
        this.mode = MagicValues.key(CommandBlockMode.class, in.readVarInt());

        int flags = in.readUnsignedByte();
        this.doesTrackOutput = (flags & FLAG_TRACK_OUTPUT) != 0;
        this.conditional = (flags & FLAG_CONDITIONAL) != 0;
        this.automatic = (flags & FLAG_AUTOMATIC) != 0;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.position);
        out.writeString(this.command);
        out.writeVarInt(MagicValues.value(Integer.class, this.mode));

        int flags = 0;
        if (this.doesTrackOutput) {
            flags |= FLAG_TRACK_OUTPUT;
        }

        if (this.conditional) {
            flags |= FLAG_CONDITIONAL;
        }

        if (this.automatic) {
            flags |= FLAG_AUTOMATIC;
        }

        out.writeByte(flags);
    }
}
