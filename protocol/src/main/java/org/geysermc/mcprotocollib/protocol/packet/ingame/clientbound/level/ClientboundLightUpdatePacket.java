package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.LightUpdateData;

import java.util.BitSet;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundLightUpdatePacket implements MinecraftPacket {
    private final int x;
    private final int z;
    private final LightUpdateData lightData;

    public ClientboundLightUpdatePacket(int x, int z, BitSet skyYMask, BitSet blockYMask,
                                        BitSet emptySkyYMask, BitSet emptyBlockYMask,
                                        List<byte[]> skyUpdates, List<byte[]> blockUpdates) {
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
        this.lightData = new LightUpdateData(skyYMask, blockYMask, emptySkyYMask, emptyBlockYMask, skyUpdates, blockUpdates);
    }

    public ClientboundLightUpdatePacket(ByteBuf in) {
        this.x = MinecraftTypes.readVarInt(in);
        this.z = MinecraftTypes.readVarInt(in);
        this.lightData = MinecraftTypes.readLightUpdateData(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.x);
        MinecraftTypes.writeVarInt(out, this.z);
        MinecraftTypes.writeLightUpdateData(out, this.lightData);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
