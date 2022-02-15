package com.github.steveice10.mc.protocol.packet.ingame.serverbound.level;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;
import java.util.Arrays;

@Data
@With
public class ServerboundSignUpdatePacket implements Packet {
    private final @NonNull Position position;
    private final @NonNull String[] lines;

    public ServerboundSignUpdatePacket(@NonNull Position position, @NonNull String[] lines) {
        if (lines.length != 4) {
            throw new IllegalArgumentException("Lines must contain exactly 4 strings.");
        }

        this.position = position;
        this.lines = Arrays.copyOf(lines, lines.length);
    }

    public ServerboundSignUpdatePacket(NetInput in) throws IOException {
        this.position = Position.read(in);
        this.lines = new String[4];
        for (int count = 0; count < this.lines.length; count++) {
            this.lines[count] = in.readString();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.position);
        for (String line : this.lines) {
            out.writeString(line);
        }
    }
}
