package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@Data
@AllArgsConstructor
public class Rotation {
    private final float pitch;
    private final float yaw;
    private final float roll;

    public static Rotation read(NetInput in) throws IOException {
        return new Rotation(in.readFloat(), in.readFloat(), in.readFloat());
    }

    public static void write(NetOutput out, Rotation rot) throws IOException {
        out.writeFloat(rot.getPitch());
        out.writeFloat(rot.getYaw());
        out.writeFloat(rot.getRoll());
    }
}
