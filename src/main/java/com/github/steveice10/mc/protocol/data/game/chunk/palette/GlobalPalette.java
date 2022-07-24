package com.github.steveice10.mc.protocol.data.game.chunk.palette;

import lombok.EqualsAndHashCode;

/**
 * A global palette that maps 1:1.
 */
@EqualsAndHashCode
public class GlobalPalette implements Palette {
    public static final GlobalPalette INSTANCE = new GlobalPalette();

    @Override
    public int size() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int stateToId(int state) {
        return state;
    }

    @Override
    public int idToState(int id) {
        return id;
    }
}
