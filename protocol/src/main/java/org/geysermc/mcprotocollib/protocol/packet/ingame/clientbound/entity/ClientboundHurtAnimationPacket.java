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
public class ClientboundHurtAnimationPacket implements MinecraftPacket {
    private final int id;
    private final float yaw;

    public ClientboundHurtAnimationPacket(ByteBuf in) {
        this.id = MinecraftTypes.readVarInt(in);
        this.yaw = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.id);
        out.writeFloat(this.yaw);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
