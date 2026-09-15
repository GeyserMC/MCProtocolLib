package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@With
public class ServerboundSignUpdatePacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final @NonNull List<String> lines;
    private final boolean isFrontText;

    public ServerboundSignUpdatePacket(@NonNull Vector3i position, @NonNull String[] lines, boolean isFrontText) {
        if (lines.length != 4) {
            throw new IllegalArgumentException("Lines must contain exactly 4 strings.");
        }

        this.position = position;
        this.lines = List.of(lines);
        this.isFrontText = isFrontText;
    }

    public ServerboundSignUpdatePacket(ByteBuf in) {
        this.position = MinecraftTypes.readPosition(in);
        this.lines = new ArrayList<>(4);
        for (int count = 0; count < 4; count++) {
            this.lines.add(MinecraftTypes.readString(in, 384));
        }
        this.isFrontText = MinecraftTypes.readVarInt(in) == 1;
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.position);
        for (String line : this.lines) {
            MinecraftTypes.writeString(out, line);
        }
        out.writeBoolean(this.isFrontText);
    }
}
