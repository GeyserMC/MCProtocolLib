package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundMoveEntityRotPacket implements MinecraftPacket {
    private final int entityId;
    private final float yaw;
    private final float pitch;
    private final boolean onGround;

    public ClientboundMoveEntityRotPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entityId = helper.readVarInt(in);
        this.yaw = in.readByte() * 360 / 256f;
        this.pitch = in.readByte() * 360 / 256f;
        this.onGround = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.entityId);
        out.writeByte((byte) (this.yaw * 256 / 360));
        out.writeByte((byte) (this.pitch * 256 / 360));
        out.writeBoolean(this.onGround);
    }
}
