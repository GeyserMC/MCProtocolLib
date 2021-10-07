package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerUpdateLightPacket implements Packet {
    private int x;
    private int z;
    private @NonNull BitSet skyYMask;
    private @NonNull BitSet blockYMask;
    private @NonNull BitSet emptySkyYMask;
    private @NonNull BitSet emptyBlockYMask;
    private @NonNull List<byte[]> skyUpdates;
    private @NonNull List<byte[]> blockUpdates;
    private boolean trustEdges;

    public ServerUpdateLightPacket(int x, int z, @NonNull BitSet skyYMask, @NonNull BitSet blockYMask,
                                   @NonNull BitSet emptySkyYMask, @NonNull BitSet emptyBlockYMask,
                                   @NonNull List<byte[]> skyUpdates, @NonNull List<byte[]> blockUpdates, boolean trustEdges) {
        for (byte[] content : skyUpdates) {
            if (content.length != 2048) {
                throw new IllegalArgumentException("All arrays in skyUpdates must be length of 2048!");
            }
        }
        for (byte[] content : blockUpdates) {
            if (content.length != 2048) {
                throw new IllegalArgumentException("All arrays in blockUpdates must be length of 2048!");
            }
        }
        this.x = x;
        this.z = z;
        this.skyYMask = skyYMask;
        this.blockYMask = blockYMask;
        this.emptySkyYMask = emptySkyYMask;
        this.emptyBlockYMask = emptyBlockYMask;
        this.skyUpdates = skyUpdates;
        this.blockUpdates = blockUpdates;
        this.trustEdges = trustEdges;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.x = in.readVarInt();
        this.z = in.readVarInt();
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

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.x);
        out.writeVarInt(this.z);
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

    @Override
    public boolean isPriority() {
        return false;
    }

    private void writeBitSet(NetOutput out, BitSet bitSet) throws IOException {
        long[] array = bitSet.toLongArray();
        out.writeVarInt(array.length);
        for (long content : array) {
            out.writeLong(content);
        }
    }
}
