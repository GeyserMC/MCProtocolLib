package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSetCameraPacket implements MinecraftPacket {
    private final int cameraEntityId;

    public ClientboundSetCameraPacket(ByteBuf in) {
        this.cameraEntityId = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.cameraEntityId);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
