package org.geysermc.mcprotocollib.protocol.data.game.chunk.palette;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;

import java.util.Arrays;

/**
 * A palette backed by a List.
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ListPalette implements Palette {
    private final int capacity;

    private final int[] data;
    private int nextId = 0;

    public ListPalette(int bitsPerEntry) {
        this.capacity = 1 << bitsPerEntry;

        this.data = new int[this.capacity];
    }

    public ListPalette(int bitsPerEntry, ByteBuf in, MinecraftCodecHelper helper) {
        this(bitsPerEntry);

        int paletteLength = helper.readVarInt(in);
        for (int i = 0; i < paletteLength; i++) {
            this.data[i] = helper.readVarInt(in);
        }

        this.nextId = paletteLength;
    }

    @Override
    public int size() {
        return this.nextId;
    }

    @Override
    public int stateToId(int state) {
        int id = -1;
        for (int i = 0; i < this.nextId; i++) { // Linear search for state
            if (this.data[i] == state) {
                id = i;
                break;
            }
        }
        if (id == -1 && this.size() < this.capacity) {
            id = this.nextId++;
            this.data[id] = state;
        }

        return id;
    }

    @Override
    public int idToState(int id) {
        if (id >= 0 && id < this.size()) {
            return this.data[id];
        } else {
            return 0;
        }
    }

    @Override
    public ListPalette copy() {
        return new ListPalette(this.capacity, Arrays.copyOf(this.data, this.data.length), this.nextId);
    }
}
