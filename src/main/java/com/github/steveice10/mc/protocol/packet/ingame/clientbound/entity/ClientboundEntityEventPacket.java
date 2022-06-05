package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.EntityEvent;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundEntityEventPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull EntityEvent event;

    public ClientboundEntityEventPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.entityId = in.readInt();
        this.event = helper.readEntityEvent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeInt(this.entityId);
        helper.writeEntityEvent(out, this.event);
    }
}
