package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.Arrays;

@Data
@With
public class ServerboundSignUpdatePacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final @NonNull String[] lines;
    private final boolean isFrontText;

    public ServerboundSignUpdatePacket(@NonNull Vector3i position, @NonNull String[] lines, boolean isFrontText) {
        if (lines.length != 4) {
            throw new IllegalArgumentException("Lines must contain exactly 4 strings.");
        }

        this.position = position;
        this.lines = Arrays.copyOf(lines, lines.length);
        this.isFrontText = isFrontText;
    }

    public ServerboundSignUpdatePacket(ByteBuf in) {
        this.position = MinecraftTypes.readPosition(in);
        this.isFrontText = in.readBoolean();
        this.lines = new String[4];
        for (int count = 0; count < this.lines.length; count++) {
            this.lines[count] = MinecraftTypes.readString(in);
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.position);
        out.writeBoolean(this.isFrontText);
        for (String line : this.lines) {
            MinecraftTypes.writeString(out, line);
        }
    }
}
