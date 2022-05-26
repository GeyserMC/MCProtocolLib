package com.github.steveice10.mc.protocol.data.game.level.particle.positionsource;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public enum PositionSourceType {
    BLOCK,
    ENTITY;

    public static PositionSourceType read(NetInput in) throws IOException {
        return MagicValues.key(PositionSourceType.class, Identifier.formalize(in.readString()));
    }

    public void write(NetOutput out) throws IOException {
        out.writeString(MagicValues.value(String.class, this));
    }
}
