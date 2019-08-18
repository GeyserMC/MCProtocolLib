package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Setter(AccessLevel.NONE)
public class Chunk {
    private static final BlockState AIR = new BlockState(0);

    private int blockCount;
    private int bitsPerEntry;

    private @NonNull List<BlockState> states;
    private @NonNull FlexibleStorage storage;

    public Chunk() {
        this.blockCount = 0;
        this.bitsPerEntry = 4;

        this.states = new ArrayList<>();
        this.states.add(AIR);

        this.storage = new FlexibleStorage(this.bitsPerEntry, 4096);
    }

    public Chunk(@NonNull NetInput in) throws IOException {
        this.blockCount = in.readShort();
        this.bitsPerEntry = in.readUnsignedByte();

        this.states = new ArrayList<>();
        int stateCount = this.bitsPerEntry > 8 ? 0 : in.readVarInt();
        for(int i = 0; i < stateCount; i++) {
            this.states.add(NetUtil.readBlockState(in));
        }

        this.storage = new FlexibleStorage(this.bitsPerEntry, in.readLongs(in.readVarInt()));
    }

    private static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    public void write(@NonNull NetOutput out) throws IOException {
        out.writeShort(this.blockCount);
        out.writeByte(this.bitsPerEntry);

        if (this.bitsPerEntry <= 8) {
            out.writeVarInt(this.states.size());
            for (BlockState state : this.states) {
                NetUtil.writeBlockState(out, state);
            }
        }

        long[] data = this.storage.getData();
        out.writeVarInt(data.length);
        out.writeLongs(data);
    }

    public BlockState get(int x, int y, int z) {
        int id = this.storage.get(index(x, y, z));
        return this.bitsPerEntry <= 8 ? (id >= 0 && id < this.states.size() ? this.states.get(id) : AIR) : new BlockState(id);
    }

    public void set(int x, int y, int z, @NonNull BlockState state) {
        int id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state.getId();
        if(id == -1) {
            this.states.add(state);
            if(this.states.size() > 1 << this.bitsPerEntry) {
                this.bitsPerEntry++;

                List<BlockState> oldStates = this.states;
                if(this.bitsPerEntry > 8) {
                    oldStates = new ArrayList<BlockState>(this.states);
                    this.states.clear();
                    this.bitsPerEntry = 13;
                }

                FlexibleStorage oldStorage = this.storage;
                this.storage = new FlexibleStorage(this.bitsPerEntry, this.storage.getSize());
                for(int index = 0; index < this.storage.getSize(); index++) {
                    this.storage.set(index, this.bitsPerEntry <= 8 ? oldStorage.get(index) : oldStates.get(index).getId());
                }
            }

            id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state.getId();
        }

        int ind = index(x, y, z);
        int curr = this.storage.get(ind);
        if(state.getId() != AIR.getId() && curr == AIR.getId()) {
            this.blockCount++;
        } else if(state.getId() == AIR.getId() && curr != AIR.getId()) {
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
