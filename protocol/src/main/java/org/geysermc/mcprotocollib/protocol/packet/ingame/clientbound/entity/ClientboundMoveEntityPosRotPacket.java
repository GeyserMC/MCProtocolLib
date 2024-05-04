package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundMoveEntityPosRotPacket implements MinecraftPacket {
    private final int entityId;
    private final double moveX;
    private final double moveY;
    private final double moveZ;
    private final float yaw;
    private final float pitch;
    private final boolean onGround;

    public ClientboundMoveEntityPosRotPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.moveX = buf.readShort() / 4096D;
        this.moveY = buf.readShort() / 4096D;
        this.moveZ = buf.readShort() / 4096D;
        this.yaw = buf.readByte() * 360 / 256f;
        this.pitch = buf.readByte() * 360 / 256f;
        this.onGround = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeShort((int) (this.moveX * 4096));
        buf.writeShort((int) (this.moveY * 4096));
        buf.writeShort((int) (this.moveZ * 4096));
        buf.writeByte((byte) (this.yaw * 256 / 360));
        buf.writeByte((byte) (this.pitch * 256 / 360));
        buf.writeBoolean(this.onGround);
    }
}
