package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetCameraPacket implements MinecraftPacket {
    private final int cameraEntityId;

    public ClientboundSetCameraPacket(MinecraftByteBuf buf) {
        this.cameraEntityId = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.cameraEntityId);
    }
}
