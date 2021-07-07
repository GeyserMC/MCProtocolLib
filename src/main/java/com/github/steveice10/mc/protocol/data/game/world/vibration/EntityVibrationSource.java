package com.github.steveice10.mc.protocol.data.game.world.vibration;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;

@AllArgsConstructor
@Getter
@ToString
public class EntityVibrationSource implements VibrationSource {
    private final int destinationEntityId;

    public static EntityVibrationSource read(NetInput in) throws IOException {
        return new EntityVibrationSource(in.readVarInt());
    }

    public static void write(NetOutput out, EntityVibrationSource vibration) throws IOException {
        out.writeVarInt(vibration.getDestinationEntityId());
    }
}
