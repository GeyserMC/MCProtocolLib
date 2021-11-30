package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.level.LightUpdateData;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.BitSet;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundLightUpdatePacket implements Packet {
    private final int x;
    private final int z;
    private final @Nonnull LightUpdateData lightData;

    public ClientboundLightUpdatePacket(int x, int z, @NonNull BitSet skyYMask, @NonNull BitSet blockYMask,
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
        this.lightData = new LightUpdateData(skyYMask, blockYMask, emptySkyYMask, emptyBlockYMask, skyUpdates, blockUpdates, trustEdges);
    }

    public ClientboundLightUpdatePacket(NetInput in) throws IOException {
        this.x = in.readVarInt();
        this.z = in.readVarInt();
        this.lightData = LightUpdateData.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.x);
        out.writeVarInt(this.z);
        LightUpdateData.write(out, this.lightData);
    }
}
