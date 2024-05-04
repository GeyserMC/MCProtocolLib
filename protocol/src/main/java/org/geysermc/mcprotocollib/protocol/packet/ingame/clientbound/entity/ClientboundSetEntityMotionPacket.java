package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetEntityMotionPacket implements MinecraftPacket {
    private final int entityId;
    private final double motionX;
    private final double motionY;
    private final double motionZ;

    public ClientboundSetEntityMotionPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.motionX = buf.readShort() / 8000D;
        this.motionY = buf.readShort() / 8000D;
        this.motionZ = buf.readShort() / 8000D;
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeShort((int) (this.motionX * 8000));
        buf.writeShort((int) (this.motionY * 8000));
        buf.writeShort((int) (this.motionZ * 8000));
    }
}
