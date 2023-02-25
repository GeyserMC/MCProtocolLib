package com.github.steveice10.mc.protocol.data.game.level.block.value;

public enum EndGatewayValueType implements BlockValueType {
    TRIGGER_BEAM;

    private static final EndGatewayValueType[] VALUES = values();

    public static EndGatewayValueType from(int id) {
        return VALUES[id];
    }
}
