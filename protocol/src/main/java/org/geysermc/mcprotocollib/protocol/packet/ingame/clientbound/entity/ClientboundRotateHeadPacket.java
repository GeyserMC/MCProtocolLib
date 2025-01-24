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
public class ClientboundRotateHeadPacket implements MinecraftPacket {
    private final int entityId;
    private final float headYaw;

    public ClientboundRotateHeadPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.headYaw = in.readByte() * 360 / 256f;
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        out.writeByte((byte) (this.headYaw * 256 / 360));
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
