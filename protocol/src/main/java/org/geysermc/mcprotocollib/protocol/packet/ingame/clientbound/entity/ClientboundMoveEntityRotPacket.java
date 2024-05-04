package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundMoveEntityRotPacket implements MinecraftPacket {
    private final int entityId;
    private final float yaw;
    private final float pitch;
    private final boolean onGround;

    public ClientboundMoveEntityRotPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.yaw = buf.readByte() * 360 / 256f;
        this.pitch = buf.readByte() * 360 / 256f;
        this.onGround = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeByte((byte) (this.yaw * 256 / 360));
        buf.writeByte((byte) (this.pitch * 256 / 360));
        buf.writeBoolean(this.onGround);
    }
}
