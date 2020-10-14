package com.github.steveice10.mc.protocol.data.game.chunk.palette;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import lombok.EqualsAndHashCode;

/**
 * A palette backed by a map.
 */
@EqualsAndHashCode
public class MapPalette implements Palette {
    private final int maxId;

    private final int[] idToState;
    private final IntObjectMap<Integer> stateToId = new IntObjectHashMap<>();
    private int nextId = 1;

    public MapPalette(int bitsPerEntry) {
        this.maxId = (1 << bitsPerEntry) - 1;

        this.idToState = new int[this.maxId + 1];
        this.stateToId.put(0, (Integer) 0);
    }

    @Override
    public int size() {
        return this.nextId;
    }

    @Override
    public int stateToId(int state) {
        Integer id = this.stateToId.get(state);
        if(id == null && this.size() < this.maxId + 1) {
            id = this.nextId++;
            this.idToState[id] = state;
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
        if(id >= 0 && id < this.size()) {
            return this.idToState[id];
        } else {
            return 0;
        }
    }
}
