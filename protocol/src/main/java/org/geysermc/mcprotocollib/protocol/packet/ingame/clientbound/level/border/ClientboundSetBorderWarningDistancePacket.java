package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSetBorderWarningDistancePacket implements MinecraftPacket {
    private final int warningBlocks;

    public ClientboundSetBorderWarningDistancePacket(ByteBuf in) {
        this.warningBlocks = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.warningBlocks);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
