package com.github.steveice10.mc.protocol.data.game.chunk.palette;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * A palette backed by a map.
 */
@EqualsAndHashCode
public class MapPalette implements Palette {
    private final int maxId;

    private final Map<Integer, Integer> idToState = new HashMap<>();
    private final Map<Integer, Integer> stateToId = new HashMap<>();
    private int nextId = 1;

    public MapPalette(int bitsPerEntry) {
        this.maxId = (1 << bitsPerEntry) - 1;

        this.idToState.put(0, 0);
        this.stateToId.put(0, 0);
    }

    @Override
    public int size() {
        return this.idToState.size();
    }

    @Override
    public int stateToId(int state) {
        Integer id = this.stateToId.get(state);
        if(id == null && this.size() < this.maxId + 1) {
            id = this.nextId++;
            this.idToState.put(id, state);
            this.stateToId.put(state, id);
        }

        if(id != null) {
            return id;
        } else {
            return -1;
        }
    }

    @Override
    public int idToState(int id) {
        Integer state = this.idToState.get(id);
        if(state != null) {
            return state;
        } else {
            return 0;
        }
    }
}
