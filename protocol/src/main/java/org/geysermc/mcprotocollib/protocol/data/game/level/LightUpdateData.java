package org.geysermc.mcprotocollib.protocol.data.game.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;

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

    public static LightUpdateData read(MinecraftByteBuf in) {
        return new LightUpdateData(in);
    }

    private LightUpdateData(MinecraftByteBuf in) {
        this.skyYMask = BitSet.valueOf(in.readLongArray());
        this.blockYMask = BitSet.valueOf(in.readLongArray());
        this.emptySkyYMask = BitSet.valueOf(in.readLongArray());
        this.emptyBlockYMask = BitSet.valueOf(in.readLongArray());

        int skyUpdateSize = in.readVarInt();
        skyUpdates = new ArrayList<>(skyUpdateSize);
        for (int i = 0; i < skyUpdateSize; i++) {
            skyUpdates.add(in.readByteArray());
        }

        int blockUpdateSize = in.readVarInt();
        blockUpdates = new ArrayList<>(blockUpdateSize);
        for (int i = 0; i < blockUpdateSize; i++) {
            blockUpdates.add(in.readByteArray());
        }
    }

    public static void write(MinecraftByteBuf out, LightUpdateData data) {
        data.write(out);
    }

    private void write(MinecraftByteBuf out) {
        writeBitSet(out, this.skyYMask);
        writeBitSet(out, this.blockYMask);
        writeBitSet(out, this.emptySkyYMask);
        writeBitSet(out, this.emptyBlockYMask);

        out.writeVarInt(this.skyUpdates.size());
        for (byte[] array : this.skyUpdates) {
            out.writeByteArray(array);
        }

        out.writeVarInt(this.blockUpdates.size());
        for (byte[] array : this.blockUpdates) {
            out.writeByteArray(array);
        }
    }

    private void writeBitSet(MinecraftByteBuf out, BitSet bitSet) {
        long[] array = bitSet.toLongArray();
        out.writeLongArray(array);
    }
}
