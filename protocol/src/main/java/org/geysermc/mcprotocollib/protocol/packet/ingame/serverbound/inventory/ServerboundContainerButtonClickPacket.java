package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundContainerButtonClickPacket implements MinecraftPacket {
    private final int containerId;
    private final int buttonId;

    public ServerboundContainerButtonClickPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = helper.readVarInt(in);
        this.buttonId = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.containerId);
        helper.writeVarInt(out, this.buttonId);
    }
}
