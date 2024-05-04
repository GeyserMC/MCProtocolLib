package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundRotateHeadPacket implements MinecraftPacket {
    private final int entityId;
    private final float headYaw;

    public ClientboundRotateHeadPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.headYaw = buf.readByte() * 360 / 256f;
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeByte((byte) (this.headYaw * 256 / 360));
    }
}
