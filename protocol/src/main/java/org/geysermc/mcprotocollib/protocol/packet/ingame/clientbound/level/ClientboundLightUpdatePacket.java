package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.level.LightUpdateData;

import java.util.BitSet;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundLightUpdatePacket implements MinecraftPacket {
    private final int x;
    private final int z;
    private final @NonNull LightUpdateData lightData;

    public ClientboundLightUpdatePacket(int x, int z, @NonNull BitSet skyYMask, @NonNull BitSet blockYMask,
                                        @NonNull BitSet emptySkyYMask, @NonNull BitSet emptyBlockYMask,
                                        @NonNull List<byte[]> skyUpdates, @NonNull List<byte[]> blockUpdates) {
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

    public ClientboundLightUpdatePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.x = helper.readVarInt(in);
        this.z = helper.readVarInt(in);
        this.lightData = helper.readLightUpdateData(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.x);
        helper.writeVarInt(out, this.z);
        helper.writeLightUpdateData(out, this.lightData);
    }
}
