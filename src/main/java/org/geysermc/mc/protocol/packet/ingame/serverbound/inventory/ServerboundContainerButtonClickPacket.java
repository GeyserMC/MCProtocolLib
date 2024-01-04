package org.geysermc.mc.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ServerboundContainerButtonClickPacket implements MinecraftPacket {
    private final int containerId;
    private final int buttonId;

    public ServerboundContainerButtonClickPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = in.readByte();
        this.buttonId = in.readByte();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeByte(this.containerId);
        out.writeByte(this.buttonId);
    }
}
