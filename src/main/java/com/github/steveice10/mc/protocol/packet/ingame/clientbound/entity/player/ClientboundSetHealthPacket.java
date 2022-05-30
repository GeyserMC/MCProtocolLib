package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSetHealthPacket implements MinecraftPacket {
    private final float health;
    private final int food;
    private final float saturation;

    public ClientboundSetHealthPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.health = in.readFloat();
        this.food = helper.readVarInt(in);
        this.saturation = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeFloat(this.health);
        helper.writeVarInt(out, this.food);
        out.writeFloat(this.saturation);
    }
}
