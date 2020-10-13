package com.github.steveice10.mc.protocol.data.game.chunk.palette;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * A palette backed by a List.
 */
@EqualsAndHashCode
public class ListPalette implements Palette {
    private final int maxId;

    private final List<Integer> data = new ArrayList<>();

    public ListPalette(int bitsPerEntry) {
        this.maxId = (1 << bitsPerEntry) - 1;

        this.data.add(0);
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public int stateToId(int state) {
        int id = this.data.indexOf(state);
        if(id == -1 && this.size() < this.maxId + 1) {
            this.data.add(id);
            id = this.data.size() - 1;
        }

        return id;
    }

    @Override
    public int idToState(int id) {
        if(id >= 0 && id < this.data.size()) {
            return this.data.get(id);
        } else {
            return 0;
        }
    }
}
