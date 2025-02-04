package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.CommandBlockMode;

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

    public ServerboundSetCommandBlockPacket(ByteBuf in) {
        this.position = MinecraftTypes.readPosition(in);
        this.command = MinecraftTypes.readString(in);
        this.mode = CommandBlockMode.from(MinecraftTypes.readVarInt(in));

        int flags = in.readUnsignedByte();
        this.doesTrackOutput = (flags & FLAG_TRACK_OUTPUT) != 0;
        this.conditional = (flags & FLAG_CONDITIONAL) != 0;
        this.automatic = (flags & FLAG_AUTOMATIC) != 0;
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.position);
        MinecraftTypes.writeString(out, this.command);
        MinecraftTypes.writeVarInt(out, this.mode.ordinal());

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
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
