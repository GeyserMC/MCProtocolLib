package com.github.steveice10.mc.protocol.data.game.chunk.palette;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

/**
 * A palette backed by a map.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class MapPalette implements Palette {
    private static final int MISSING_ID = -1;
    private final int size;

    private final int[] idToState;
    private final Int2IntMap stateToId = new Int2IntOpenHashMap();
    private int nextId = 0;

    public MapPalette(int bitsPerEntry) {
        this.size = 1 << bitsPerEntry;

        this.idToState = new int[this.size];
        this.stateToId.defaultReturnValue(MISSING_ID);
    }

    public MapPalette(int bitsPerEntry, ByteBuf in, MinecraftCodecHelper helper) {
        this(bitsPerEntry);

        int paletteLength = helper.readVarInt(in);
        for (int i = 0; i < paletteLength; i++) {
            int state = helper.readVarInt(in);
            this.idToState[i] = state;
            this.stateToId.putIfAbsent(state, i);
        }
        this.nextId = paletteLength;
    }

    @Override
    public int size() {
        return this.nextId;
    }

    @Override
    public int stateToId(int state) {
        int id = this.stateToId.get(state);
        if (id == MISSING_ID && this.size() < this.size) {
            id = this.nextId++;
            this.idToState[id] = state;
            this.stateToId.put(state, id);
        }

        return id;
    }

    @Override
    public int idToState(int id) {
        if (id >= 0 && id < this.size()) {
            return this.idToState[id];
        } else {
            return 0;
        }
    }

    @Override
    public MapPalette copy() {
        MapPalette mapPalette = new MapPalette(this.size, Arrays.copyOf(this.idToState, this.idToState.length), this.nextId);
        mapPalette.stateToId.putAll(this.stateToId);
        return mapPalette;
    }
}
