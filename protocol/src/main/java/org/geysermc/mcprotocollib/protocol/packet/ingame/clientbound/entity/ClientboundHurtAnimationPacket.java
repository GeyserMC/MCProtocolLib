package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundHurtAnimationPacket implements MinecraftPacket {
    private final int id;
    private final float yaw;

    public ClientboundHurtAnimationPacket(MinecraftByteBuf buf) {
        this.id = buf.readVarInt();
        this.yaw = buf.readFloat();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.id);
        buf.writeFloat(this.yaw);
    }
}
