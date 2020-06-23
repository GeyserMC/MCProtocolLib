package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class Chunk {
    private static final int AIR = 0;

    private int blockCount;
    private int bitsPerEntry;

    private @NonNull List<Integer> palette;
    private @NonNull FlexibleStorage storage;

    public Chunk() {
        this(0, 4, new ArrayList<>(Collections.singletonList(AIR)), new FlexibleStorage(4));
    }

    public static Chunk read(NetInput in) throws IOException {
        int blockCount = in.readShort();
        int bitsPerEntry = in.readUnsignedByte();

        List<Integer> palette = new ArrayList<>();
        if(bitsPerEntry <= 8) {
            int paletteLength = in.readVarInt();
            for(int i = 0; i < paletteLength; i++) {
                palette.add(in.readVarInt());
            }
        }

        FlexibleStorage storage = new FlexibleStorage(bitsPerEntry, in.readLongs(in.readVarInt()));
        return new Chunk(blockCount, bitsPerEntry, palette, storage);
    }

    public static void write(NetOutput out, Chunk chunk) throws IOException {
        out.writeShort(chunk.getBlockCount());
        out.writeByte(chunk.getBitsPerEntry());

        if(chunk.getBitsPerEntry() <= 8) {
            out.writeVarInt(chunk.getPalette().size());
            for (int state : chunk.getPalette()) {
                out.writeVarInt(state);
            }
        }

        long[] data = chunk.getStorage().getData();
        out.writeVarInt(data.length);
        out.writeLongs(data);
    }

    private static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    public int get(int x, int y, int z) {
        int id = this.storage.get(index(x, y, z));
        return this.bitsPerEntry <= 8 ? (id >= 0 && id < this.palette.size() ? this.palette.get(id) : AIR) : id;
    }

    public void set(int x, int y, int z, @NonNull int state) {
        int id = this.bitsPerEntry <= 8 ? this.palette.indexOf(state) : state;
        if(id == -1) {
            this.palette.add(state);
            if(this.palette.size() > 1 << this.bitsPerEntry) {
                this.bitsPerEntry++;

                final List<Integer> oldStates = this.bitsPerEntry > 8 ? new ArrayList<>(this.palette) : this.palette;
                if(this.bitsPerEntry > 8) {
                    this.palette.clear();
                    this.bitsPerEntry = 14;
                }

                FlexibleStorage oldStorage = this.storage;
                this.storage = oldStorage.transferData(this.bitsPerEntry, (index) ->
                        this.bitsPerEntry <= 8 ? oldStorage.get(index) : oldStates.get(index));
            }

            id = this.bitsPerEntry <= 8 ? this.palette.indexOf(state) : state;
        }

        int ind = index(x, y, z);
        int curr = this.storage.get(ind);
        if(state != AIR && curr == AIR) {
            this.blockCount++;
        } else if(state == AIR && curr != AIR) {
            this.blockCount--;
        }

        this.storage.set(ind, id);
    }

    public boolean isEmpty() {
        for(int index = 0; index < this.storage.getSize(); index++) {
            if(this.storage.get(index) != 0) {
                return false;
            }
        }

        return true;
    }
}
