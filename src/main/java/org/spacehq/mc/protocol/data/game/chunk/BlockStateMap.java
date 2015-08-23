package org.spacehq.mc.protocol.data.game.chunk;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BlockStateMap {
    private static final int[] MAX_ID_PER_BYTES = { 15, 255, 4095 };

    private BlockStorage parent;

    private Map<Integer, Integer> idToState = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> stateToId = new HashMap<Integer, Integer>();
    private int stateCount = 0;

    private int currentSize = 0;

    public BlockStateMap(BlockStorage parent) {
        this.parent = parent;

        this.idToState.put(0, 0);
        this.stateToId.put(0, 0);
        this.stateCount++;
    }

    public BlockStateMap(BlockStorage parent, NetInput in) throws IOException {
        this.parent = parent;

        this.stateCount = in.readVarInt();
        for(int i = 0; i < this.stateCount; i++) {
            int state = in.readVarInt();
            int id = in.readVarInt();

            this.idToState.put(id, state);
            this.stateToId.put(state, id);
        }
    }

    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.stateCount);
        for(Map.Entry<Integer, Integer> entry : this.stateToId.entrySet()) {
            out.writeVarInt(entry.getKey());
            out.writeVarInt(entry.getValue());
        }
    }

    public Map<Integer, Integer> getIdsToStates() {
        return new HashMap<Integer, Integer>(this.idToState);
    }

    public Map<Integer, Integer> getStatesToIds() {
        return new HashMap<Integer, Integer>(this.stateToId);
    }

    public int getState(int id) {
        return this.idToState.containsKey(id) ? this.idToState.get(id) : 0;
    }

    public int getId(int state) {
        if(!this.stateToId.containsKey(state)) {
            int id = this.stateCount;
            this.idToState.put(id, state);
            this.stateToId.put(state, id);

            if(this.currentSize < MAX_ID_PER_BYTES.length - 1 && this.stateCount > MAX_ID_PER_BYTES[this.currentSize]) {
                this.parent.resize(MAX_ID_PER_BYTES[++this.currentSize]);
            }

            this.stateCount++;
        }

        return this.stateToId.get(state);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof BlockStateMap && this.idToState.equals(((BlockStateMap) o).idToState) && this.stateToId.equals(((BlockStateMap) o).stateToId) && this.stateCount == ((BlockStateMap) o).stateCount && this.currentSize == ((BlockStateMap) o).currentSize);
    }

    @Override
    public int hashCode() {
        int result = this.idToState.hashCode();
        result = 31 * result + this.stateToId.hashCode();
        result = 31 * result + this.stateCount;
        result = 31 * result + this.currentSize;
        return result;
    }
}
