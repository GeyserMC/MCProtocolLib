package org.geysermc.mc.protocol.packet.ingame.clientbound.inventory;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundContainerClosePacket implements MinecraftPacket {
    private final int containerId;

    public ClientboundContainerClosePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = in.readUnsignedByte();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeByte(this.containerId);
    }
}
