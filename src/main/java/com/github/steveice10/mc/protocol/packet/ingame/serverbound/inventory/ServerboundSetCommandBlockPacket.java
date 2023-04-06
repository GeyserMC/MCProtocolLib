package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.level.block.CommandBlockMode;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundSetCommandBlockPacket implements MinecraftPacket {
    private static final int FLAG_TRACK_OUTPUT = 0x01;
    private static final int FLAG_CONDITIONAL = 0x02;
    private static final int FLAG_AUTOMATIC = 0x04;

    private final @NonNull Vector3i position;
    private final @NonNull String command;
    private final @NonNull CommandBlockMode mode;
    private final boolean doesTrackOutput;
    private final boolean conditional;
    private final boolean automatic;

    public ServerboundSetCommandBlockPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.position = helper.readPosition(in);
        this.command = helper.readString(in);
        this.mode = CommandBlockMode.from(helper.readVarInt(in));

        int flags = in.readUnsignedByte();
        this.doesTrackOutput = (flags & FLAG_TRACK_OUTPUT) != 0;
        this.conditional = (flags & FLAG_CONDITIONAL) != 0;
        this.automatic = (flags & FLAG_AUTOMATIC) != 0;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writePosition(out, this.position);
        helper.writeString(out, this.command);
        helper.writeVarInt(out, this.mode.ordinal());

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
