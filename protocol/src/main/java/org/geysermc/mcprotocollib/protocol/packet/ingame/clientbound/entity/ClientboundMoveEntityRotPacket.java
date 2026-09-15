package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundMoveEntityRotPacket implements MinecraftPacket {
    private final int entityId;
    private final float yaw;
    private final float pitch;
    private final boolean onGround;

    public ClientboundMoveEntityRotPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.onGround = in.readBoolean();
        this.yaw = in.readByte() * 360 / 256f;
        this.pitch = in.readByte() * 360 / 256f;
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        out.writeBoolean(this.onGround);
        out.writeByte((byte) (this.yaw * 256 / 360));
        out.writeByte((byte) (this.pitch * 256 / 360));
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
