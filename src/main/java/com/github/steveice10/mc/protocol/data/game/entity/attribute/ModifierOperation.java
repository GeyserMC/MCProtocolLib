package com.github.steveice10.mc.protocol.data.game.entity.attribute;

import com.github.steveice10.packetlib.io.NetInput;

import java.io.IOException;

public enum ModifierOperation {
    ADD,
    ADD_MULTIPLIED,
    MULTIPLY;

    private static final ModifierOperation[] VALUES = values();

    public static ModifierOperation read(NetInput in) throws IOException {
        return VALUES[in.readByte()];
    }
}
