package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.player.Animation;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundAnimatePacket implements MinecraftPacket {
    private final int entityId;
    private final @Nullable Animation animation;

    public ClientboundAnimatePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.entityId = helper.readVarInt(in);
        this.animation = Animation.from(in.readUnsignedByte());
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.entityId);
        if (this.animation == null) {
            out.writeByte(-1); // Client does nothing on unknown ID
        } else {
            out.writeByte(this.animation.getId());
        }
    }
}
