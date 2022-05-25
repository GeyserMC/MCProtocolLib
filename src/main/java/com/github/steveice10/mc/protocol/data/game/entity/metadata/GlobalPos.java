package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.nukkitx.math.vector.Vector3i;

import java.io.IOException;

public class GlobalPos {
    private final String dimension;
    private final Vector3i position;

    public GlobalPos(String dimension, Vector3i position) {
        this.dimension = dimension;
        this.position = position;
    }

    public String getDimension() {
        return dimension;
    }

    public Vector3i getPosition() {
        return position;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getZ() {
        return position.getZ();
    }

    public static GlobalPos read(NetInput in) throws IOException {
        String dimension = Identifier.formalize(in.readString());
        Vector3i pos = Position.readNukkit(in);
        return new GlobalPos(dimension, pos);
    }

    public static void write(NetOutput out, GlobalPos pos) throws IOException {
        out.writeString(pos.getDimension());
        Position.write(out, pos.getPosition());
    }
}
