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
public class ClientboundSetBorderWarningDelayPacket implements MinecraftPacket {
    private final int warningDelay;

    public ClientboundSetBorderWarningDelayPacket(ByteBuf in) {
        this.warningDelay = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.warningDelay);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
