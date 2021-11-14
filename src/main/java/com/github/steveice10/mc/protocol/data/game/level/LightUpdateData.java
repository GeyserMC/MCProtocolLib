package com.github.steveice10.mc.protocol.data.game.level;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@Data
@AllArgsConstructor
public class LightUpdateData {
    private final @NonNull BitSet skyYMask;
    private final @NonNull BitSet blockYMask;
    private final @NonNull BitSet emptySkyYMask;
    private final @NonNull BitSet emptyBlockYMask;
    private final @NonNull List<byte[]> skyUpdates;
    private final @NonNull List<byte[]> blockUpdates;
    private final boolean trustEdges;

    public static LightUpdateData read(NetInput in) throws IOException {
        return new LightUpdateData(in);
    }

    private LightUpdateData(NetInput in) throws IOException {
        this.trustEdges = in.readBoolean();

        this.skyYMask = BitSet.valueOf(in.readLongs(in.readVarInt()));
        this.blockYMask = BitSet.valueOf(in.readLongs(in.readVarInt()));
        this.emptySkyYMask = BitSet.valueOf(in.readLongs(in.readVarInt()));
        this.emptyBlockYMask = BitSet.valueOf(in.readLongs(in.readVarInt()));

        int skyUpdateSize = in.readVarInt();
        skyUpdates = new ArrayList<>(skyUpdateSize);
        for (int i = 0; i < skyUpdateSize; i++) {
            skyUpdates.add(in.readBytes(in.readVarInt()));
        }

        int blockUpdateSize = in.readVarInt();
        blockUpdates = new ArrayList<>(blockUpdateSize);
        for (int i = 0; i < blockUpdateSize; i++) {
            blockUpdates.add(in.readBytes(in.readVarInt()));
        }
    }

    public static void write(NetOutput out, LightUpdateData data) throws IOException {
        data.write(out);
    }

    private void write(NetOutput out) throws IOException {
        out.writeBoolean(this.trustEdges);

        writeBitSet(out, this.skyYMask);
        writeBitSet(out, this.blockYMask);
        writeBitSet(out, this.emptySkyYMask);
        writeBitSet(out, this.emptyBlockYMask);

        out.writeVarInt(this.skyUpdates.size());
        for (byte[] array : this.skyUpdates) {
            out.writeVarInt(array.length);
            out.writeBytes(array);
        }

        out.writeVarInt(this.blockUpdates.size());
        for (byte[] array : this.blockUpdates) {
            out.writeVarInt(array.length);
            out.writeBytes(array);
        }
    }

    private void writeBitSet(NetOutput out, BitSet bitSet) throws IOException {
        long[] array = bitSet.toLongArray();
        out.writeVarInt(array.length);
        for (long content : array) {
            out.writeLong(content);
        }
    }
}
