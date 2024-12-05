package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundContainerClosePacket implements MinecraftPacket {
    private final int containerId;

    public ClientboundContainerClosePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.containerId);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
