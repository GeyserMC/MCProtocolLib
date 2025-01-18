package org.geysermc.mcprotocollib.protocol.data.game.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

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

    public static LightUpdateData read(ByteBuf in) {
        return new LightUpdateData(in);
    }

    private LightUpdateData(ByteBuf in) {
        this.skyYMask = BitSet.valueOf(MinecraftTypes.readLongArray(in));
        this.blockYMask = BitSet.valueOf(MinecraftTypes.readLongArray(in));
        this.emptySkyYMask = BitSet.valueOf(MinecraftTypes.readLongArray(in));
        this.emptyBlockYMask = BitSet.valueOf(MinecraftTypes.readLongArray(in));

        int skyUpdateSize = MinecraftTypes.readVarInt(in);
        skyUpdates = new ArrayList<>(skyUpdateSize);
        for (int i = 0; i < skyUpdateSize; i++) {
            skyUpdates.add(MinecraftTypes.readByteArray(in));
        }

        int blockUpdateSize = MinecraftTypes.readVarInt(in);
        blockUpdates = new ArrayList<>(blockUpdateSize);
        for (int i = 0; i < blockUpdateSize; i++) {
            blockUpdates.add(MinecraftTypes.readByteArray(in));
        }
    }

    public static void write(ByteBuf out, LightUpdateData data) {
        data.write(out);
    }

    private void write(ByteBuf out) {
        writeBitSet(out, this.skyYMask);
        writeBitSet(out, this.blockYMask);
        writeBitSet(out, this.emptySkyYMask);
        writeBitSet(out, this.emptyBlockYMask);

        MinecraftTypes.writeVarInt(out, this.skyUpdates.size());
        for (byte[] array : this.skyUpdates) {
            MinecraftTypes.writeByteArray(out, array);
        }

        MinecraftTypes.writeVarInt(out, this.blockUpdates.size());
        for (byte[] array : this.blockUpdates) {
            MinecraftTypes.writeByteArray(out, array);
        }
    }

    private void writeBitSet(ByteBuf out, BitSet bitSet) {
        long[] array = bitSet.toLongArray();
        MinecraftTypes.writeLongArray(out, array);
    }
}
