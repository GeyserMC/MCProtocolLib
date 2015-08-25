package org.spacehq.mc.protocol.data.game.chunk;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockStateMap {
    private static final int[] MAX_ID_PER_BYTES = { 15, 255, 4095 };

    private BlockStorage parent;

    private List<Integer> states = new ArrayList<Integer>();
    private int currentMaxValue = 0;

    public BlockStateMap(BlockStorage parent) {
        this.parent = parent;

        this.states.add(0);
    }

    public BlockStateMap(BlockStorage parent, NetInput in) throws IOException {
        int stateCount = in.readVarInt();
        for(int i = 0; i < stateCount; i++) {
            this.addState(in.readVarInt());
        }

        this.parent = parent;
    }

    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.states.size());
        for(int state : this.states) {
            out.writeVarInt(state);
        }
    }

    public List<Integer> getStates() {
        return new ArrayList<Integer>(this.states);
    }

    public int getState(int id) {
        return id >= 0 && id < this.states.size() ? this.states.get(id) : 0;
    }

    public int getId(int state) {
        int id = this.states.indexOf(state);
        if(id == -1) {
            id = this.addState(state);
        }

        return id;
    }

    private int addState(int state) {
        this.states.add(state);
        if(this.currentMaxValue < MAX_ID_PER_BYTES.length - 1 && this.states.size() > MAX_ID_PER_BYTES[this.currentMaxValue]) {
            this.currentMaxValue++;
            if(this.parent != null) {
                this.parent.resize(MAX_ID_PER_BYTES[this.currentMaxValue]);
            }
        }

        return this.states.indexOf(state);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof BlockStateMap && this.states.equals(((BlockStateMap) o).states) && this.currentMaxValue == ((BlockStateMap) o).currentMaxValue);
    }

    @Override
    public int hashCode() {
        int result = this.states.hashCode();
        result = 31 * result + this.currentMaxValue;
        return result;
    }
}
