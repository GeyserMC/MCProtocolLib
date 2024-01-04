package com.github.steveice10.mc.protocol.data.game.chunk.palette;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * A palette containing one state.
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public class SingletonPalette implements Palette {
    private final int state;

    @Override
    public int size() {
        return 1;
    }

    @Override
    public int stateToId(int state) {
        return this.state == state ? 0 : -1;
    }

    @Override
    public int idToState(int id) {
        return id == 0 ? this.state : 0;
    }

    @Override
    public SingletonPalette copy() {
        return this;
    }
}
