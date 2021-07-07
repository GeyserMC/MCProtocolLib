package com.github.steveice10.mc.protocol.data.game.world.vibration;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;

@AllArgsConstructor
@Getter
@ToString
public class BlockVibrationSource implements VibrationSource {
    private final Position destinationPos;

    public static BlockVibrationSource read(NetInput in) throws IOException {
        return new BlockVibrationSource(Position.read(in));
    }

    public static void write(NetOutput out, BlockVibrationSource vibration) throws IOException {
        Position.write(out, vibration.getDestinationPos());
    }
}
