package com.github.steveice10.mc.protocol.data.game.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import io.netty.buffer.ByteBuf;
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

    public static LightUpdateData read(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        return new LightUpdateData(in, helper);
    }

    private LightUpdateData(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.skyYMask = BitSet.valueOf(helper.readLongArray(in));
        this.blockYMask = BitSet.valueOf(helper.readLongArray(in));
        this.emptySkyYMask = BitSet.valueOf(helper.readLongArray(in));
        this.emptyBlockYMask = BitSet.valueOf(helper.readLongArray(in));

        int skyUpdateSize = helper.readVarInt(in);
        skyUpdates = new ArrayList<>(skyUpdateSize);
        for (int i = 0; i < skyUpdateSize; i++) {
            skyUpdates.add(helper.readByteArray(in));
        }

        int blockUpdateSize = helper.readVarInt(in);
        blockUpdates = new ArrayList<>(blockUpdateSize);
        for (int i = 0; i < blockUpdateSize; i++) {
            blockUpdates.add(helper.readByteArray(in));
        }
    }

    public static void write(ByteBuf out, MinecraftCodecHelper helper, LightUpdateData data) throws IOException {
        data.write(out, helper);
    }

    private void write(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        writeBitSet(out, helper, this.skyYMask);
        writeBitSet(out, helper, this.blockYMask);
        writeBitSet(out, helper, this.emptySkyYMask);
        writeBitSet(out, helper, this.emptyBlockYMask);

        helper.writeVarInt(out, this.skyUpdates.size());
        for (byte[] array : this.skyUpdates) {
            helper.writeByteArray(out, array);
        }

        helper.writeVarInt(out, this.blockUpdates.size());
        for (byte[] array : this.blockUpdates) {
            helper.writeByteArray(out, array);
        }
    }

    private void writeBitSet(ByteBuf out, MinecraftCodecHelper helper, BitSet bitSet) throws IOException {
        long[] array = bitSet.toLongArray();
        helper.writeLongArray(out, array);
    }
}
