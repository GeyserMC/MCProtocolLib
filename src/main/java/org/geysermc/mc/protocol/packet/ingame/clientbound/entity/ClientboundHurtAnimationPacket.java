package org.geysermc.mc.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundHurtAnimationPacket implements MinecraftPacket {
    private final int id;
    private final float yaw;

    public ClientboundHurtAnimationPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.id = helper.readVarInt(in);
        this.yaw = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.id);
        out.writeFloat(this.yaw);
    }
}
