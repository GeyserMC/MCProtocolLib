package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundContainerButtonClickPacket implements MinecraftPacket {
    private final int containerId;
    private final int buttonId;

    public ServerboundContainerButtonClickPacket(ByteBuf in) {
        this.containerId = MinecraftTypes.readVarInt(in);
        this.buttonId = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.containerId);
        MinecraftTypes.writeVarInt(out, this.buttonId);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
