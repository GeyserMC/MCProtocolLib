package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

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
public class ClientboundCooldownPacket implements MinecraftPacket {
    private final int itemId;
    private final int cooldownTicks;

    public ClientboundCooldownPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.itemId = helper.readVarInt(in);
        this.cooldownTicks = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.itemId);
        helper.writeVarInt(out, this.cooldownTicks);
    }
}
