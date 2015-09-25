package org.spacehq.mc.protocol.data.game.chunk;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockStorage {
    private int bitsPerEntry;

    private List<Integer> states;
    private FlexibleStorage storage;

    public BlockStorage() {
        this.bitsPerEntry = 4;

        this.states = new ArrayList<Integer>();
        this.states.add(0);

        this.storage = new FlexibleStorage(this.bitsPerEntry, 4096);
    }

    public BlockStorage(NetInput in) throws IOException {
        this.bitsPerEntry = in.readUnsignedByte();

        this.states = new ArrayList<Integer>();
        int stateCount = in.readVarInt();
        for(int i = 0; i < stateCount; i++) {
            this.states.add(in.readVarInt());
        }

        this.storage = new FlexibleStorage(this.bitsPerEntry, in.readLongs(in.readVarInt()));
    }

    public void write(NetOutput out) throws IOException {
        out.writeByte(this.bitsPerEntry);

        out.writeVarInt(this.states.size());
        for(int state : this.states) {
            out.writeVarInt(state);
        }

        long[] data = this.storage.getData();
        out.writeVarInt(data.length);
        out.writeLongs(data);
    }

    public int getBitsPerEntry() {
        return this.bitsPerEntry;
    }

    public List<Integer> getStates() {
        return Collections.unmodifiableList(this.states);
    }

    public FlexibleStorage getStorage() {
        return this.storage;
    }

    public int get(int x, int y, int z) {
        int id = this.storage.get(index(x, y, z));
        return this.bitsPerEntry <= 8 ? (id >= 0 && id < this.states.size() ? this.states.get(id) : 0) : id;
    }

    public void set(int x, int y, int z, int state) {
        int id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state;
        if(id == -1) {
            this.states.add(state);
            if(this.states.size() > 1 << this.bitsPerEntry) {
                this.bitsPerEntry++;

                List<Integer> oldStates = this.states;
                if(this.bitsPerEntry > 8) {
                    oldStates = new ArrayList<Integer>(this.states);
                    this.states.clear();
                    this.bitsPerEntry = 13;
                }

                FlexibleStorage oldStorage = this.storage;
                this.storage = new FlexibleStorage(this.bitsPerEntry, this.storage.getSize());
                for(int index = 0; index < this.storage.getSize(); index++) {
                    int value = oldStorage.get(index);
                    this.storage.set(index, this.bitsPerEntry <= 8 ? value : oldStates.get(value));
                }
            }

            id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state;
        }

        this.storage.set(index(x, y, z), id);
    }

    public boolean isEmpty() {
        for(int index = 0; index < this.storage.getSize(); index++) {
            if(this.storage.get(index) != 0) {
                return false;
            }
        }

        return true;
    }

    private static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof BlockStorage && this.bitsPerEntry == ((BlockStorage) o).bitsPerEntry && this.states.equals(((BlockStorage) o).states) && this.storage.equals(((BlockStorage) o).storage));
    }

    @Override
    public int hashCode() {
        int result = this.bitsPerEntry;
        result = 31 * result + this.states.hashCode();
        result = 31 * result + this.storage.hashCode();
        return result;
    }
}
