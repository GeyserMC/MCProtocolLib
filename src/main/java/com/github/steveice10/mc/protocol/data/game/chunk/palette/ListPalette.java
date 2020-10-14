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

    private final int[] data;
    private int nextId = 1;

    public ListPalette(int bitsPerEntry) {
        this.maxId = (1 << bitsPerEntry) - 1;

        this.data = new int[this.maxId + 1];
    }

    @Override
    public int size() {
        return this.nextId;
    }

    @Override
    public int stateToId(int state) {
        int id = -1;
        for(int i = 0; i < this.nextId; i++) { // Linear search for state
            if(this.data[i] == state) {
                id = i;
                break;
            }
        }
        if(id == -1 && this.size() < this.maxId + 1) {
            id = this.nextId++;
            this.data[id] = state;
        }

        return id;
    }

    @Override
    public int idToState(int id) {
        if(id >= 0 && id < this.size()) {
            return this.data[id];
        } else {
            return 0;
        }
    }
}
