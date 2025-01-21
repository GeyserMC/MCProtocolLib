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
public class ClientboundMoveEntityPosPacket implements MinecraftPacket {
    private final int entityId;
    private final double moveX;
    private final double moveY;
    private final double moveZ;
    private final boolean onGround;

    public ClientboundMoveEntityPosPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.moveX = in.readShort() / 4096D;
        this.moveY = in.readShort() / 4096D;
        this.moveZ = in.readShort() / 4096D;
        this.onGround = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        out.writeShort((int) (this.moveX * 4096));
        out.writeShort((int) (this.moveY * 4096));
        out.writeShort((int) (this.moveZ * 4096));
        out.writeBoolean(this.onGround);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
