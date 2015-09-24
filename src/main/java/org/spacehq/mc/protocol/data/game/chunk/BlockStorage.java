package org.spacehq.mc.protocol.data.game.chunk;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;

import java.io.IOException;

public class BlockStorage {
    private BlockStateMap stateMap;
    private FlexibleStorage storage;

    public BlockStorage() {
        this.stateMap = new BlockStateMap(this);
        this.storage = new FlexibleStorage(4, 4096);
    }

    public BlockStorage(NetInput in) throws IOException {
        this.stateMap = new BlockStateMap(this, in);
        this.storage = new FlexibleStorage(in);
    }

    public void write(NetOutput out) throws IOException {
        this.stateMap.write(out);
        this.storage.write(out);
    }

    public BlockStateMap getStateMap() {
        return this.stateMap;
    }

    public FlexibleStorage getStorage() {
        return this.storage;
    }

    public int get(int x, int y, int z) {
        return this.stateMap.getState(this.storage.get(index(x, y, z)));
    }

    public void set(int x, int y, int z, int state) {
        int id = this.stateMap.getId(state);
        this.storage.set(index(x, y, z), id);
    }

    public boolean isEmpty() {
        for(int index = 0; index < this.storage.getSize(); index++) {
            if(this.storage.get(index) != 0) {
                return true;
            }
        }

        return false;
    }

    protected void resize(int size) {
        FlexibleStorage oldStorage = this.storage;
        this.storage = new FlexibleStorage(size != 0 ? 32 - Integer.numberOfLeadingZeros(size - 1) : 0, this.storage.getSize());
        for(int index = 0; index < this.storage.getSize(); index++) {
            this.storage.set(index, oldStorage.get(index));
        }
    }

    private static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof BlockStorage && this.stateMap.equals(((BlockStorage) o).stateMap) && this.storage.equals(((BlockStorage) o).storage));
    }

    @Override
    public int hashCode() {
        int result = this.stateMap.hashCode();
        result = 31 * result + this.storage.hashCode();
        return result;
    }
}
