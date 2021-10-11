package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.CommandBlockMode;
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
public class ClientUpdateCommandBlockPacket implements Packet {
    private static final int FLAG_TRACK_OUTPUT = 0x01;
    private static final int FLAG_CONDITIONAL = 0x02;
    private static final int FLAG_AUTOMATIC = 0x04;

    private @NonNull Position position;
    private @NonNull String command;
    private @NonNull CommandBlockMode mode;
    private boolean doesTrackOutput;
    private boolean conditional;
    private boolean automatic;

    @Override
    public void read(NetInput in) throws IOException {
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
