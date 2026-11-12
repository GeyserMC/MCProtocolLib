package org.geysermc.mcprotocollib.protocol.data.game.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.BitSet;
import java.util.List;

@Data
@AllArgsConstructor
public class LightUpdateData {
    private final BitSet skyYMask;
    private final BitSet blockYMask;
    private final BitSet emptySkyYMask;
    private final BitSet emptyBlockYMask;
    private final List<byte[]> skyUpdates;
    private final List<byte[]> blockUpdates;

    public static LightUpdateData read(ByteBuf in) {
        return MinecraftTypes.readLightUpdateData(in);
    }

    public static void write(ByteBuf out, LightUpdateData data) {
        MinecraftTypes.writeLightUpdateData(out, data);
    }
}
